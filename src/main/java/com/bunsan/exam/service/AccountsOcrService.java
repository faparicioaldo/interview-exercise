package com.bunsan.exam.service;

import java.io.IOException;

public interface AccountsOcrService {

	public void extractAccountNumbers(String sourceFileName, String targetFileName) throws IOException;
}
