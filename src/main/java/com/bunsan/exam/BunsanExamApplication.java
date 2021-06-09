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
	public void run(String... args) {
		try {
			accountsOcrService.extractAccountNumbers();

		} catch (IOException e) {
			log.error("IOException", e);
		}
	}
}
