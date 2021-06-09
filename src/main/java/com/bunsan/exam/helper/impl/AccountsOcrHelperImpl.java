package com.bunsan.exam.helper.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bunsan.exam.helper.AccountsOcrHelper;
import com.bunsan.exam.util.Constants;

@Component
public class AccountsOcrHelperImpl implements AccountsOcrHelper{

    private static final Logger log = LogManager.getLogger(AccountsOcrHelperImpl.class);

	/**
	 * Write in a file the results of process input list (account number with pipes and underscore)
	 * @param real input list (account number with pipes and underscore convert to real numbers)
	 * */
	public void writeResults(List<String> response, String pathToWriteResult) throws IOException {
		File file = null;
		try (BufferedWriter bwriter  = new BufferedWriter(new FileWriter(file = new File(pathToWriteResult)))){
			if (!file.exists()) {
				file.createNewFile();				
			}
			
			for (String res : response) {
				bwriter.write(res + "\n");
			}
			
		} catch (Exception e) {
			log.error("Error al escribir archivo, verificar permisos", e);
		}
	}

	/**
	 * Read a file and get input list (account number with pipes and underscore)
	 * @return input list (account number with pipes and underscore)
	 * */
	public Map<Integer, List<String>> fileToInputs(String fileNameInClasspath) throws IOException {
		Map<Integer, List<String>> inputs = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileNameInClasspath))) {
			inputs = new HashMap<>();	
			
			String currentLine = "";
			List<String> input = new ArrayList<>();
			int i = 1, counter = 1;
			while ((currentLine = reader.readLine()) != null) {
				if (i % 4 == 0) {// Restart the each 4 rows
					inputs.put(counter, input);
					input = new ArrayList<>();
					counter++;
				} else {// row 4 is ignored
					input.add(currentLine);
				}				
				i++;
			}
		} catch (Exception e) {
			log.error("No se encontro el archivo o no tiene permisos para leerlo");
		}

		return inputs;
	}

	/**
	 * Given a input list (account number with pipes and underscore) get real account numbers
	 * @param input list (account number with pipes and underscore)
	 * @return list with real account numbers
	 * */
	public Map<String, String> extractAccountNumbers(Map<Integer, List<String>> inputs) {
		Map<String, String> accountNumbers = null;
		try {
			accountNumbers = new HashMap<>();
			for (Entry<Integer, List<String>> input : inputs.entrySet()) {// Iterate inputs

				List<Integer> decimalAccountNumber = inputToDecimal(input);

				String status = "OK";
				if (decimalAccountNumber.contains(-1)) {
					status = "ILL";
				} else if (!validateAccountNumber(decimalAccountNumber)) {
					status = "ERR";
				}
				accountNumbers.put(
						decimalAccountNumber.stream()
						.map(x -> x.equals(-1) ? "?" : x.toString())
						.collect(Collectors.joining("")),
						status);
			}

		} catch (Exception e) {
			log.error("El archivo contiene lineas con menos de 27 caracteres, por favor validar", e);
		}
		return accountNumbers;
	}

	/**
	 * Given a input (account number with pipes and underscore) convert to binary and finally in decimal
	 * @param input (account number with pipes and underscore)
	 * @return true:valid | false:invalid
	 * */
	public List<Integer> inputToDecimal(Entry<Integer, List<String>> input) {
		List<Integer> decimal = null;
		String binary = null;
		try {
			decimal = new ArrayList<>();
			
			// entry set get only 3 first lines, 4 is empty and no neccesary 
			String inputLine1 = input.getValue().get(0);
			String inputLine2 = input.getValue().get(1);
			String inputLine3 = input.getValue().get(2);
			
			int ini = 0;
			int fin = 3;
			for (int i = 0; i < 9; i++) {
				String display1 = inputLine1.substring(ini, fin);
				String display2 = inputLine2.substring(ini, fin);
				String display3 = inputLine3.substring(ini, fin);
	
				binary = segmentsToBinary(display1 + display2 + display3);
				
				if (binary == null) {
					decimal.add(-1);
				} else {
					decimal.add(Integer.parseInt(binary, 2));
				}
	
				ini = fin;
				fin += 3;
			}
		}catch(Exception e) {
			log.error("Ocurrio un problema al leer el archivo");
		}
		return decimal;
	}

	/**
	 * Given a display 7 segments unordered it sort and convert to binary 
	 * @param 7 segments string unordered
	 * @return true:valid | false:invalid
	 * */
	public String segmentsToBinary(String segmentos) {
		String binary = null;
		try {
			
			if(segmentos.length()<9) {return null;};
			
			StringBuilder sevenSegments = new StringBuilder();
			final char A_SEGMENT = segmentos.charAt(1);
			final char B_SEGMENT = segmentos.charAt(5);
			final char C_SEGMENT = segmentos.charAt(8);
			final char D_SEGMENT = segmentos.charAt(7);
			final char E_SEGMENT = segmentos.charAt(6);
			final char F_SEGMENT = segmentos.charAt(3);
			final char G_SEGMENT = segmentos.charAt(4);

			sevenSegments
				.append(A_SEGMENT != ' ' ? 1 : 0)
				.append(B_SEGMENT != ' ' ? 1 : 0)
				.append(C_SEGMENT != ' ' ? 1 : 0)
				.append(D_SEGMENT != ' ' ? 1 : 0)
				.append(E_SEGMENT != ' ' ? 1 : 0)
				.append(F_SEGMENT != ' ' ? 1 : 0)
				.append(G_SEGMENT != ' ' ? 1 : 0);

			binary = Constants.MAP_SEGMENTS_BINARY.get(sevenSegments.toString());
		} catch (Exception e) {
			log.error("El archivo contiene lineas con menos de 27 caracteres, por favor validar", e);
		}
		return binary;
	}

	/**
	 * Account number validation with checksum
	 * @param accountNumber
	 * @return true:valid | false:invalid
	 * */
	public boolean validateAccountNumber(List<Integer> accountNumber) {
		boolean accountNumberValid = false;
		try {

			List<Integer> accountNumberReverse = new ArrayList<>();
			accountNumberReverse.addAll(accountNumber);
			Collections.reverse(accountNumberReverse);
			int suma = 0;

			for (int i = 0; i < 9; i++) {
				suma += (i + 1) * accountNumberReverse.get(i);
			}
			if (suma % 11 == 0) {
				accountNumberValid = true;
			}
		} catch (Exception e) {
			log.error("El archivo contiene lineas con menos de 27 caracteres, por favor validar", e);
		}
		return accountNumberValid;
	}
}
