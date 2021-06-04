

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

	//Path to save Input and Results, please change if is neccesary 
//	String basePathFiles = "C:"+File.separator+"tmp"+File.separator;
	String basePathFiles = "";
	String fileNameInputs = "inputs.txt";
	String fileNameResults = "results.txt";
	
	/**
	 * Given a file with pipes and underscores
	 * <ul>
	 * <li>Get inputs from file to HashMap grouping by item<li> 
	 * <li>Get real account numbers from each input<li> 
	 * <li>Validate inputs<li> 
	 * <li>Write results into a file and in console (only unique accounts no repeated)<li> 
	 * @param args no in use
	 * */
	public static void main(String[] args) throws IOException {

		Principal principal = new Principal();
		Map<Integer, List<String>> inputs = principal.fileToInputs();

		Map<String, String> accountNumbers = principal.extractAccountNumbers(inputs);
		List<String> response = accountNumbers.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.map(ME -> ME.getKey() + " " + ME.getValue())
				.peek(System.out::println)
				.collect(Collectors.toList());		

		principal.writeResults(response);
	}

	/**
	 * Write in a file the results of process input list (account number with pipes and underscore)
	 * @param real input list (account number with pipes and underscore convert to real numbers)
	 * */
	public void writeResults(List<String> response) throws IOException {
		File file = null;
		FileWriter writer = null;
		BufferedWriter bwriter = null;
		try {
			file = new File(basePathFiles+fileNameResults);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file);
			bwriter = new BufferedWriter(writer);
			for (String res : response) {
				bwriter.write(res + "\n");
			}
			
		} catch (Exception e) {
			System.err.println("Error al escribir archivo, verificar permisos" + e);
		}finally {
			bwriter.close();
		}
	}

	/**
	 * Read a file and get input list (account number with pipes and underscore)
	 * @return input list (account number with pipes and underscore)
	 * */
	public Map<Integer, List<String>> fileToInputs() throws IOException {
		BufferedReader reader = null;
		Map<Integer, List<String>> inputs = null;
		try {
			inputs = new HashMap<Integer, List<String>>();
			reader = new BufferedReader(new FileReader(basePathFiles+fileNameInputs));

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
			System.err.println("Error al leer archivo, verificar permisos" + e);
		} finally {
			reader.close();
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

				List<Integer> decimal = inputToDecimal(input);

				String status = "OK";
				if (decimal.contains(-1)) {
					status = "ILL";
				} else if (!validateAccountNumber(decimal)) {
					status = "ERR";
				}
				accountNumbers.put(
						decimal.stream()
						.map(x -> x.equals(-1) ? "?" : x.toString())
						.collect(Collectors.joining("")),
						status);
			}

		} catch (Exception e) {
			System.err.println("El archivo contiene lineas con menos de 27 caracteres, por favor validar" + e);
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
			System.err.println("El archivo contiene lineas con menos de 27 caracteres, por favor validar" + e);
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
			System.err.println("El archivo contiene lineas con menos de 27 caracteres, por favor validar" + e);
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
			System.err.println("El archivo contiene lineas con menos de 27 caracteres, por favor validar" + e);
		}
		return accountNumberValid;
	}

	public class Response {
		private String status;
		private String accountNumber;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

	}

	public static class Constants {
		public final static Map<String, String> MAP_SEGMENTS_BINARY = new HashMap<>();
		static{
			MAP_SEGMENTS_BINARY.put("1111110", "0000");
			MAP_SEGMENTS_BINARY.put("0110000", "0001");
			MAP_SEGMENTS_BINARY.put("1101101", "0010");
			MAP_SEGMENTS_BINARY.put("1111001", "0011");
			MAP_SEGMENTS_BINARY.put("0110011", "0100");
			MAP_SEGMENTS_BINARY.put("1011011", "0101");
			MAP_SEGMENTS_BINARY.put("1011111", "0110");
			MAP_SEGMENTS_BINARY.put("1110000", "0111");
			MAP_SEGMENTS_BINARY.put("1111111", "1000");
			MAP_SEGMENTS_BINARY.put("1111011", "1001");
		}
	}
}
