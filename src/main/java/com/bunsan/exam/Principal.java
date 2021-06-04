package com.bunsan.exam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class Principal {

	public static void main(String[] args) throws IOException {

		Principal principal = new Principal();
		Map<Integer, List<String>> inputs = principal.fileToInputs();
//		inputs.forEach(a->a.forEach(x->System.err.println(x)));

		Map<String, String> accountNumbers = principal.extractAccountNumbers(inputs);
		List<String> response = accountNumbers.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.map(ME -> ME.getKey() + " " + ME.getValue()).peek(System.out::println).collect(Collectors.toList());
		;

		principal.writeResponse(response);
	}

	public void writeResponse(List<String> response) {
		try {
			String ruta = "C:\\tmp\\result.txt";
			File file = new File(ruta);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file);
			BufferedWriter bwriter = new BufferedWriter(writer);
			for (String res : response) {
				bwriter.write(res + "\n");
			}

			bwriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, List<String>> fileToInputs() throws IOException {
		String file = "C:\\tmp\\tests.txt";
		BufferedReader reader = null;
		Map<Integer, List<String>> inputs = null;
		try {
			inputs = new HashMap<Integer, List<String>>();
			reader = new BufferedReader(new FileReader(file));

			String currentLine = "";
			List<String> input = new ArrayList<String>();
			int i = 1;
			while ((currentLine = reader.readLine()) != null) {
				if (i % 4 == 0) {// Restart the each 4 rows
					inputs.put(i, input);
					input = new ArrayList<String>();
				} else {// row 4 is ignored
					input.add(currentLine);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}

		return inputs;
	}

	public Map<String, String> extractAccountNumbers(Map<Integer, List<String>> inputs) {
		Map<String, String> accountNumbers = null;
		try {
			accountNumbers = new HashMap<>();
			for (Entry<Integer, List<String>> input : inputs.entrySet()) {// Itera entradas
				// grupo de una entrada
				String inputLine1 = input.getValue().get(0);
				String inputLine2 = input.getValue().get(1);
				String inputLine3 = input.getValue().get(2);

				System.err.println(inputLine1);
				System.err.println(inputLine2);
				System.err.println(inputLine3);

				int ini = 0;
				int fin = 3;

				List<Integer> decimal = new ArrayList<>();
				for (int i = 0; i < 9; i++) {

					String display1 = inputLine1.substring(ini, fin);
					String display2 = inputLine2.substring(ini, fin);
					String display3 = inputLine3.substring(ini, fin);

					String binary = segmentsToBinary(display1 + display2 + display3);
					System.err.println(binary);
					if (binary == null) {
						decimal.add(-1);
					} else {
						decimal.add(Integer.parseInt(binary, 2));
					}

					ini = fin;
					fin += 3;
				}
				String status = "OK";
				if (decimal.contains(-1)) {
					status = "ILL";
				} else if (!validateAccountNumber(decimal)) {
					status = "ERR";
				}
				accountNumbers.put(
						decimal.stream().map(x -> x.equals(-1) ? "?" : x.toString()).collect(Collectors.joining("")),
						status);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountNumbers;
	}

	public String segmentsToBinary(String segmentos) {
		String binary = null;
		try {
			StringBuilder sevenSegments = new StringBuilder();
			final char A_SEGMENT = segmentos.charAt(1);
			final char B_SEGMENT = segmentos.charAt(5);
			final char C_SEGMENT = segmentos.charAt(8);
			final char D_SEGMENT = segmentos.charAt(7);
			final char E_SEGMENT = segmentos.charAt(6);
			final char F_SEGMENT = segmentos.charAt(3);
			final char G_SEGMENT = segmentos.charAt(4);

			sevenSegments.append(A_SEGMENT != ' ' ? 1 : 0).append(B_SEGMENT != ' ' ? 1 : 0)
					.append(C_SEGMENT != ' ' ? 1 : 0).append(D_SEGMENT != ' ' ? 1 : 0).append(E_SEGMENT != ' ' ? 1 : 0)
					.append(F_SEGMENT != ' ' ? 1 : 0).append(G_SEGMENT != ' ' ? 1 : 0);

			binary = Constants.MAP_SEGMENTS_BINARY.get(sevenSegments.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return binary;
	}

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
			e.printStackTrace();
		}
		return accountNumberValid;
	}

}
