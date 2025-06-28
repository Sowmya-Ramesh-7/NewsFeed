package com.newsfeed.util;

import java.util.Scanner;
import java.util.regex.Pattern;

import com.newsfeed.util.constants.Constants;
import com.newsfeed.util.constants.Messages;

public class InputUtil {

	private static final Scanner scanner = new Scanner(System.in);

	public static String readLine(String prompt) {
		System.out.println("\n" + prompt);
		return scanner.nextLine();
	}

	public static String readString(String prompt) {
		System.out.println("\n" + prompt);
		return scanner.next();
	}

	public static String readLine(String prompt, String pattern, String errorMessage) {
		int attempts = 0;
		while (hasAttemptsLeft(attempts)) {
			System.out.println("\n" + prompt);
			String input = scanner.nextLine();
			if (!input.isBlank() && Pattern.matches(pattern, input)) {
				return input;
			}
			attempts++;
			System.out.println(errorMessage + Messages.PLEASE_TRY_AGAIN);
		}
		throw new IllegalArgumentException(Messages.EXCEEDED_MAXIMUM_INPUT_ATTEMPTS);
	}

	public static String readString(String prompt, String pattern, String errorMessage) {
		int attempts = 0;
		while (hasAttemptsLeft(attempts)) {
			System.out.println("\n" + prompt);
			String input = scanner.next();
			if (!input.isBlank() && Pattern.matches(pattern, input)) {
				return input;
			}
			attempts++;
			System.out.println(errorMessage + Messages.PLEASE_TRY_AGAIN);
		}
		throw new IllegalArgumentException(Messages.EXCEEDED_MAXIMUM_INPUT_ATTEMPTS);
	}

	public static int readInt(String prompt, int maxAttempts) {
		System.out.println("\n" + prompt);
		int attempts = 0;
		while (hasAttemptsLeft(attempts)) {
			if (scanner.hasNextInt()) {
				int input = scanner.nextInt();
				scanner.next();
				return input;
			} else {
				attempts++;
				System.out.println(Messages.INVALID_NUMBER);
				scanner.next();
			}
		}
		throw new IllegalArgumentException(Messages.EXCEEDED_MAXIMUM_INPUT_ATTEMPTS);
	}

	public static long readLong(String prompt) {
		System.out.println("\n" + prompt);
		int attempts = 0;
		while (hasAttemptsLeft(attempts)) {
			if (scanner.hasNextLong()) {
				long input = scanner.nextLong();
				scanner.nextLine();
				return input;
			} else {
				attempts++;
				System.out.println(Messages.INVALID_NUMBER);
				scanner.next();
			}
		}
		throw new IllegalArgumentException(Messages.EXCEEDED_MAXIMUM_INPUT_ATTEMPTS);
	}

	public static boolean hasAttemptsLeft(int currentAttempt) {
		return currentAttempt < Constants.MAXIMUM_INPUT_ATTEMPTS;
	}
}
