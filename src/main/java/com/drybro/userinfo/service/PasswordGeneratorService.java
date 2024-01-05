package com.drybro.userinfo.service;

import java.util.Locale;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
public class PasswordGeneratorService {

	private final static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final static String lower = upper.toLowerCase( Locale.ROOT);
	private final static String digits = "0123456789";

	private final static String alphanum = upper + lower + digits;

	private final static char[] symbols = alphanum.toCharArray();

	private final static Random random = new Random();

	public static String generatePassword() {
		char[] generatedPassword = new char[20];
		for(int i=0; i<generatedPassword.length; i++) {
			generatedPassword[i] = symbols[random.nextInt(symbols.length)];
		}
		return new String(generatedPassword);
	}

}
