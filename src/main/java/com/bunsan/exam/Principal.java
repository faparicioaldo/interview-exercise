package com.bunsan.exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Principal {

	public static void main(String[] args) throws IOException {

		Principal principal = new Principal();
		List<List<String>> inputs = principal.fileToInputs();
		principal.extractAccountNumbers(inputs);
	}

	public List<List<String>> fileToInputs() throws IOException {
		String file = "D:\\PROJECTS\\Profesional\\entrevistas\\bunsan\\BankOCR\\src\\main\\java\\com\\bunsan\\exam\\tests.txt";
		BufferedReader reader = null;
		List<List<String>> entradas = null;
		try {
			entradas = new ArrayList<List<String>>();
			reader = new BufferedReader(new FileReader(file));

			String currentLine = "";
			List<String> entrada = new ArrayList<String>();
			int i = 1;
			while ((currentLine = reader.readLine()) != null) {
				if (i % 4 == 0) {
					entradas.add(entrada);
					entrada = new ArrayList<String>();
				}else {
					entrada.add(currentLine);
				}
				i++;
			}
			entradas.forEach(a->a.forEach(x->System.err.println(x)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}

		return entradas;
	}

	public List<String> extractAccountNumbers(List<List<String>> inputs) {
		List<String> accountNumbers = null;
		try {
		accountNumbers = new ArrayList<>();
		int iterator = 1; 
		for (List<String> input : inputs) {//Itera entradas			
			//grupo de una entrada
			String inputLine1 = input.get(0);
			String inputLine2 = input.get(1);
			String inputLine3 = input.get(2);

			System.out.println(inputLine1);
			System.out.println(inputLine2);
			System.out.println(inputLine3);

			System.out.println();
			int ini = 0;
			int fin = 3;

			int itera = 1;
			//obtiene 9 numeros de cada display
			String decimal = "";
			for (int i = 0; i < 9; i++) {

				String display1 = inputLine1.substring(ini, fin);
				String display2 = inputLine2.substring(ini, fin);
				String display3 = inputLine3.substring(ini, fin);
				System.out.print("> " + itera + " : " + " : ");

				String binary = segmentsToBinary(display1 + display2 + display3);
				decimal += binary!=null?Integer.parseInt(binary, 2):"?";
				System.out.print(binary);
				ini = fin;
				fin += 3;
				itera++;
				System.out.println();
			}
			accountNumbers.add(decimal);
			iterator++;
		}
		accountNumbers.forEach(System.err::println);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return accountNumbers;
	}

	public String segmentsToBinary(String segmentos) {
		String binary=null;
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
		}catch(Exception e) {
			e.printStackTrace();
		}
		return binary;
	}

}
