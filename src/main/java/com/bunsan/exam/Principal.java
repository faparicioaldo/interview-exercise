package com.bunsan.exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Principal {

	public static void main(String[] args) throws IOException {

		Principal principal = new Principal();
		principal.fileToInputs();
	}

	public Map<Integer, List<String>> fileToInputs() throws IOException {
		String file = "D:\\PROJECTS\\Profesional\\entrevistas\\bunsan\\BunsanTest\\bunsan-test\\src\\main\\java\\com\\bunsan\\exam\\tests.txt";
		BufferedReader reader = null;
		Map<Integer, List<String>> entradas = null;
		try {
			entradas = new HashMap<Integer, List<String>>();
			reader = new BufferedReader(new FileReader(file));
			
			String currentLine = "";
			List<String> entrada = new ArrayList<String>();
			int i = 1;
			int j = 1;
			while((currentLine = reader.readLine()) != null) {
				if(i%4==0) {
					entradas.put(j, entrada);
					entrada = new ArrayList<String>();	
					j++;
				}
				entrada.add(currentLine);				
				i++;
			}
			
		} catch (Exception e) {

		} finally {
			reader.close();
		}
		
		return entradas;
	}
	
}
