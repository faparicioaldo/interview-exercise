package com.bunsan.exam.helper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface AccountsOcrHelper {

	public void writeResults(List<String> response, String pathToWriteResult) throws IOException;

	public Map<Integer, List<String>> fileToInputs(String path) throws IOException;

	public Map<String, String> extractAccountNumbers(Map<Integer, List<String>> inputs);

	public List<Integer> inputToDecimal(Entry<Integer, List<String>> input);

	public String segmentsToBinary(String segmentos);

	public boolean validateAccountNumber(List<Integer> accountNumber);

}
