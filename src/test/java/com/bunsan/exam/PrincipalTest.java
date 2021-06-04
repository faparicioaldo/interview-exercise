package com.bunsan.exam;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PrincipalTest {

	private Principal principal;
	
	@Before
	public void init() {
		principal  = new Principal();
	}
	
	@Test
	public void fileToInputsTest() throws IOException {		
		Map<Integer, List<String>> entradas = principal.fileToInputs();
		assertEquals(780, entradas.size());	    
	}

}
