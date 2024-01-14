package com.drybro.userinfo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordGeneratorTest {

	@Test
	public void generatePassword_HappyPath() {
		final String generatedPassword = PasswordGeneratorService.generatePassword();
		assertEquals(20, generatedPassword.length());
	}

}
