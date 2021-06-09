package com.bunsan.exam.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.bunsan.exam.helper.impl.AccountsOcrHelperImpl;

class AccountsOcrHelperTest {

	private AccountsOcrHelperImpl accountsOcrHelper = new AccountsOcrHelperImpl();

	private List<String> dataInFile = 
			Arrays.asList(
					"000000000 OK",
					"222222222 NOK",
					"11111?111 ILL"
					);
	
	final List<Integer> decimalAccountNumber_1_OK = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	final List<Integer> decimalAccountNumber_2_OK = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0);
	final List<Integer> decimalAccountNumber_3_OK = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0);
	final List<Integer> decimalAccountNumber_4_OK = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0);
	final List<Integer> decimalAccountNumber_5_OK = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0);

	final List<Integer> decimalAccountNumber_1_NOK = Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1);
	final List<Integer> decimalAccountNumber_2_NOK = Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2);
	final List<Integer> decimalAccountNumber_3_NOK = Arrays.asList(3, 3, 3, 3, 3, 3, 3, 3, 3);
	final List<Integer> decimalAccountNumber_4_NOK = Arrays.asList(4, 4, 4, 4, 4, 4, 4, 4, 4);
	final List<Integer> decimalAccountNumber_5_NOK = Arrays.asList(5, 5, 5, 5, 5, 5, 5, 5, 5);

	final String SEVEN_SEGMENTS_NUMBER_0_OK = " _ | ||_|";
	final String SEVEN_SEGMENTS_NUMBER_1_OK = "     |  |";
	final String SEVEN_SEGMENTS_NUMBER_2_OK = " _  _||_ ";
	final String SEVEN_SEGMENTS_NUMBER_3_OK = " _  _| _|";
	final String SEVEN_SEGMENTS_NUMBER_4_OK = "   |_|  |";
	final String SEVEN_SEGMENTS_NUMBER_5_OK = " _ |_  _|";
	final String SEVEN_SEGMENTS_NUMBER_6_OK = " _ |_ |_|";
	final String SEVEN_SEGMENTS_NUMBER_7_OK = " _   |  |";
	final String SEVEN_SEGMENTS_NUMBER_8_OK = " _ |_||_|";
	final String SEVEN_SEGMENTS_NUMBER_9_OK = " _ |_| _|";

	final String SEVEN_SEGMENTS_NUMBER_0_NOK = " _ | || |";
	final String SEVEN_SEGMENTS_NUMBER_1_NOK = "     | _ |";
	final String SEVEN_SEGMENTS_NUMBER_2_NOK = " _  _ ";
	final String SEVEN_SEGMENTS_NUMBER_3_NOK = " _  __ _";
	final String SEVEN_SEGMENTS_NUMBER_4_NOK = "   |_";
	final String SEVEN_SEGMENTS_NUMBER_5_NOK = " _ |_  _ |    _";

	final List<Integer> SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_ARRAY_RES = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0);
	final List<Integer> SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_ARRAY_RES = Arrays.asList(1, 1, -1, 1, 1, 1, -1, 1, 1);
	final List<Integer> SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_ARRAY_RES = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	final List<Integer> SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_ARRAY_RES = Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1);
	
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_LINE_1_OK = " _  _  _  _  _  _  _  _  _ ";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_LINE_2_OK = "| || || || || || || || || |";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_LINE_3_OK = "|_||_||_||_||_||_||_||_||_|";

	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_LINE_1_OK = "                           ";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_LINE_2_OK = "  |  |     |  |  |     |  |";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_LINE_3_OK = "  |  |  |  |  |  |  |  |  |";

	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_1_OK = "    _  _     _  _  _  _  _ ";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_2_OK = "  | _| _||_||_ |_   ||_||_|";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_3_OK = "  ||_  _|  | _||_|  ||_| _|";

	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_1_OK = "                           ";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_2_OK = "  |  |  |  |  |  |  |  |  |";
	final String SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_3_OK = "  |  |  |  |  |  |  |  |  |";

	final Map<String, String> mapResult = Stream.of(
					  new AbstractMap.SimpleEntry<>("111111111", "ERR"), 
					  new AbstractMap.SimpleEntry<>("11?111?11", "ILL"),
					  new AbstractMap.SimpleEntry<>("000000000", "OK"),
					  new AbstractMap.SimpleEntry<>("123456789", "OK"))
					  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	
	final Map<Integer, List<String>> inputsOK = 
		Stream.of(
			new AbstractMap.SimpleEntry<>(1, Arrays.asList(
			   SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_LINE_1_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_LINE_2_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_LINE_3_OK
			  )),
			new AbstractMap.SimpleEntry<>(2, Arrays.asList(
			   SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_LINE_1_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_LINE_2_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_LINE_3_OK
			  )),
			new AbstractMap.SimpleEntry<>(3, Arrays.asList(
			   SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_1_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_2_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_3_OK
			  )),
			new AbstractMap.SimpleEntry<>(4, Arrays.asList(
			   SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_1_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_2_OK
			  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_3_OK
			  ))
			).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	
//	final Map<Integer, List<String>> inputsOK = 
//			Stream.of(
//				new AbstractMap.SimpleEntry<>(1, Arrays.asList(
//				   "                           "
//				  ,"   |  |  |  |  |  |  |  |  |"
//				  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_LINE_3_OK
//				  )),
//				new AbstractMap.SimpleEntry<>(2, Arrays.asList(
//				   "  _  _  _  _  _  _  _  _  _ "
//				  ," | || || || || || || || || |"
//				  ," |_||_||_||_||_||_||_||_||_|"
//				  )),
//				new AbstractMap.SimpleEntry<>(3, Arrays.asList(
//				   SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_1_OK
//				  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_2_OK
//				  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_LINE_3_OK
//				  )),
//				new AbstractMap.SimpleEntry<>(4, Arrays.asList(
//				   SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_1_OK
//				  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_2_OK
//				  ,SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_LINE_3_OK
//				  ))
//				).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	
//	{16=[                           ,
//	                                |  |  |  |  |  |  |  |  |,
//	                                |  |  |  |  |  |  |  |  |], 
//			4=[ _  _  _  _  _  _  _  _  _ , | || || || || || || || || |, |_||_||_||_||_||_||_||_||_|], 
//			8=[                           ,   |  |     |  |  |     |  |,   |  |  |  |  |  |  |  |  |], 
//			12=[    _  _     _  _  _  _  _ ,   | _| _||_||_ |_   ||_||_|,   ||_  _|  | _||_|  ||_| _|]}
	
	@Test
	void validateAccountNumberTestOK() {

		assertEquals(true, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_1_OK));
		assertEquals(true, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_2_OK));
		assertEquals(true, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_3_OK));
		assertEquals(true, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_4_OK));
		assertEquals(true, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_5_OK));

	}
	
	@Test
	void validateAccountNumberTestNOK() {

		assertEquals(false, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_1_NOK));
		assertEquals(false, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_2_NOK));
		assertEquals(false, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_3_NOK));
		assertEquals(false, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_4_NOK));
		assertEquals(false, accountsOcrHelper.validateAccountNumber(decimalAccountNumber_5_NOK));

	}
	
	@Test
	void segmentsToBinaryTestOK() {

		assertEquals("0000", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_0_OK));
		assertEquals("0001", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_1_OK));
		assertEquals("0010", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_2_OK));
		assertEquals("0011", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_3_OK));
		assertEquals("0100", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_4_OK));
		assertEquals("0101", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_5_OK));
		assertEquals("0110", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_6_OK));
		assertEquals("0111", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_7_OK));
		assertEquals("1000", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_8_OK));
		assertEquals("1001", accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_9_OK));

	}
	
	@Test
	void segmentsToBinaryTestNOK() {

		assertEquals(null, accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_0_NOK));
		assertEquals(null, accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_1_NOK));
		assertEquals(null, accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_2_NOK));
		assertEquals(null, accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_3_NOK));
		assertEquals(null, accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_4_NOK));
		assertEquals(null, accountsOcrHelper.segmentsToBinary(SEVEN_SEGMENTS_NUMBER_5_NOK));

	}
	
	@Test
	void inputToDecimalTestOK() {
		Iterator<Entry<Integer, List<String>>> iterator = inputsOK.entrySet().iterator();
		assertEquals(SEVEN_SEGMENTS_ACCOUNT_NUMBER_0_ARRAY_RES, accountsOcrHelper.inputToDecimal(iterator.next()));
		assertEquals(SEVEN_SEGMENTS_ACCOUNT_NUMBER_1_ARRAY_RES, accountsOcrHelper.inputToDecimal(iterator.next()));
		assertEquals(SEVEN_SEGMENTS_ACCOUNT_NUMBER_2_ARRAY_RES, accountsOcrHelper.inputToDecimal(iterator.next()));
		assertEquals(SEVEN_SEGMENTS_ACCOUNT_NUMBER_3_ARRAY_RES, accountsOcrHelper.inputToDecimal(iterator.next()));
	}
	
	@Test
	void extractAccountNumbersTest() {		
		assertEquals(mapResult, accountsOcrHelper.extractAccountNumbers(inputsOK));
	}
	
	@Test
	void fileToInputsTest() throws IOException {		
		String fileName = "inputs2.txt";
		assertEquals(inputsOK, accountsOcrHelper.fileToInputs(fileName));
	}
	
	@Test
	void writeResultsTest() throws IOException {		
		String fileName = "salida2.txt";
		accountsOcrHelper.writeResults(dataInFile,fileName);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			for(String row : dataInFile) {
				assertEquals(row, reader.readLine());
			}
		} catch (Exception e) {
			fail("error");
		}

	}
}
