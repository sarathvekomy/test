package com.vekomy.vbooks.util;

import java.util.Random;

public class KeyGenerationUtils {

	private final static String smallLetters = "abcdefghijklmnopqrstwzyz";
	private final static String bigLetters = "ABCDEFGHIJKLMNOPQRSTVUWXYZ";
	private final static String numbers = "1234567890";
	private final static String symbols = "@#$%&*-+";

	public static String generatePassword(String schoolName, String branchName, int size) {
		schoolName = schoolName.trim();
		branchName = branchName.trim();
		Random randomGenerator = new Random();
		StringBuffer stringBuffer = new StringBuffer();

		for (int idx = 1; idx <= size; ++idx) {
			stringBuffer.append(schoolName.substring(0, randomGenerator.nextInt(3)));
			stringBuffer.append(randomGenerator.nextInt(9999));
			stringBuffer.append(branchName.substring(0, randomGenerator.nextInt(3)));
		}

		return stringBuffer.toString();
	}

	public static String getDeactivationKey() {

		StringBuffer stringBuffer = new StringBuffer();

		Random randomGenerator = new Random();
		for (int idx = 1; idx <= 2; ++idx) {
			
			stringBuffer.append(symbols.substring(0, randomGenerator.nextInt(symbols.length() - 1)));
			stringBuffer.append(smallLetters.substring(0, randomGenerator.nextInt(smallLetters.length() - 1)));
			stringBuffer.append(randomGenerator.nextInt(9999));
			stringBuffer.append(bigLetters.substring(0, randomGenerator.nextInt(bigLetters.length() - 1)));
			stringBuffer.append(numbers.substring(0, randomGenerator.nextInt(numbers.length() - 1)));

		} 

		return stringBuffer.toString().substring(0,25);
	}
	public static void main(String[] args) {
		System.out.println(KeyGenerationUtils.getDeactivationKey().length());
    }

}
