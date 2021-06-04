package com.bunsan.exam;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PrincipalTest {

	private Principal principal;
	
	@Before
	public void init() {
		principal  = new Principal();
	}
	
	@Ignore
	@Test
	public void fileToInputsTest() throws IOException {		
		List<List<String>> entradas = principal.fileToInputs();
		assertEquals(780, entradas.size());	    
	}
	
	@Test
	public void extractSegmentsFromInputsTest() throws IOException {		
		List<List<String>> entradas = principal.fileToInputs();
		principal.extractAccountNumbers(entradas);
			    
	}

}
