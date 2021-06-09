package com.bunsan.exam.service.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bunsan.exam.helper.impl.AccountsOcrHelperImpl;
import com.bunsan.exam.service.AccountsOcrService;

@Service
public class AccountsOcrServiceImpl implements AccountsOcrService{

    private static final Logger log = LogManager.getLogger(AccountsOcrServiceImpl.class);
		
	private AccountsOcrHelperImpl accountsOcrHelper;
	
	final String INPUTS_FILE_NAME_IN_CLASSPATH = "inputs.txt";
	final String RESULTS_FILE_NAME_IN_CLASSPATH = "results.txt";
	
	/**
	 * Given a file with pipes and underscores
	 * <ul>
	 * <li>Get inputs from file to HashMap grouping by item<li> 
	 * <li>Get real account numbers from each input<li> 
	 * <li>Validate inputs<li> 
	 * <li>Write results into a file and in console (only unique accounts no repeated)<li> 
	 * @param args no in use
	 * */
	@Override
	public void extractAccountNumbers() throws IOException {
		accountsOcrHelper = new AccountsOcrHelperImpl();
		Map<Integer, List<String>> inputs = accountsOcrHelper.fileToInputs(INPUTS_FILE_NAME_IN_CLASSPATH);
		
		Map<String, String> accountNumbers = accountsOcrHelper.extractAccountNumbers(inputs);
		List<String> response = accountNumbers.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.map(ME -> ME.getKey() + " " + ME.getValue())
				.peek(x->log.info(x))
				.collect(Collectors.toList());		

		accountsOcrHelper.writeResults(response, RESULTS_FILE_NAME_IN_CLASSPATH);
		
	}

	
}
