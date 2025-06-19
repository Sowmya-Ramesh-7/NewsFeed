package com.newsfeed.util;

import java.util.regex.Pattern;

public class InputValidator {
	private static final int MAX_NUM_OF_ATTEMPT = 3;

	public static boolean isPatternMatched(String string, String pattern) {
		return Pattern.matches(pattern, string);
	}

	public static boolean isMinLength(String string, int minLength) {
		return string.length() < minLength;
	}

	public static boolean isValidOption(int choice, int totalChoices) {
		return choice > totalChoices && choice < 0;
	}

	public static boolean isValidAttempt(int numOfAttempts) {
		return numOfAttempts <= MAX_NUM_OF_ATTEMPT;
	}
}