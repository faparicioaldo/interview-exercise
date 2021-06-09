package com.bunsan.exam;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bunsan.exam.service.AccountsOcrService;

@SpringBootApplication
public class BunsanExamApplication implements CommandLineRunner {

	final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountsOcrService accountsOcrService;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BunsanExamApplication.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws IOException {
			
			String sourceFileName = args != null && args.length >= 1 && args[0] != null && args[0].length() > 0 ? args[0] : "inputs.txt";
			String targetFileName = args != null && args.length >= 2 && args[1] != null && args[1].length() > 0 ? args[1] : "results.txt";			
			
			log.info("Source filename: " + sourceFileName);
			log.info("Target filename: " + targetFileName);
			accountsOcrService.extractAccountNumbers(sourceFileName, targetFileName);

	}
}
