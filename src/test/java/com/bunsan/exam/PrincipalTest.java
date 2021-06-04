package com.bunsan.exam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class PrincipalTest {

	private Principal principal;
	
	@Before
	public void init() {
		principal  = new Principal();
	}
	
	@Test
	public void fileToInputsTestOK() throws IOException {		
		Map<Integer, List<String>> entradas = principal.fileToInputs();
		assertTrue(entradas.size()>0);	    
	}
	
	@Test
	public void extractSegmentsFromInputsTest() throws IOException {		
		Map<Integer, List<String>> entradas = principal.fileToInputs();
		principal.extractAccountNumbers(entradas);
			    
	}

	@Test
	public void segmentsToBinaryOKTest() throws IOException {		
		String segmentos=" _ | ||_|";
		String binaryExpected = "0000";
		String binary = principal.segmentsToBinary(segmentos);
		assertEquals(binaryExpected, binary);
			    
	}
	
	@Test
	public void segmentsToBinaryBADTest() throws IOException {		
		String segmentos=" _ | || |";
		String binaryExpected = null;
		String binary = principal.segmentsToBinary(segmentos);
		assertEquals(binaryExpected, binary);			    
	}
	
	@Test
	public void validateAccountNumberOKTest() throws IOException {		
		List<Integer> accountNumber = Stream.of(1,2, 3, 4, 5, 6, 7, 8, 9).collect(Collectors.toList());
		boolean isValid = principal.validateAccountNumber(accountNumber );
		assertTrue(isValid);
		accountNumber = Stream.of(0,0,0,0,0,0,0,0,0).collect(Collectors.toList());
		isValid = principal.validateAccountNumber(accountNumber );
		assertTrue(isValid);
	}
	
	@Test
	public void validateAccountNumberBADTest() throws IOException {		
		List<Integer> accountNumber = Stream.of(1,1,1,1,1,1,1,1,1).collect(Collectors.toList());
		boolean isValid = principal.validateAccountNumber(accountNumber );
		assertTrue(!isValid);
	}

}
