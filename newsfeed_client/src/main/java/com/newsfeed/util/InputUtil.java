package com.newsfeed.util;

import java.util.Scanner;

import com.newsfeed.util.constants.Messages;

public class InputUtil {
	private static final Scanner scanner = new Scanner(System.in);

	public static String readLine(String prompt) {
		System.out.println(prompt);
		return scanner.nextLine();
	}

	public static String readString(String prompt) {
		System.out.println(prompt);
		return scanner.next();
	}

	public static int readInt(String prompt, int maxAttempts) {
		System.out.println(prompt);
		int attempts = 0;

		while (attempts < maxAttempts) {
			if (scanner.hasNextInt()) {
				return scanner.nextInt();
			} else {
				attempts++;
				System.out.println(
						"Invalid input. Please enter a valid number. Attempt " + attempts + " of " + maxAttempts);
				scanner.next();
			}
		}
		throw new IllegalArgumentException(Messages.EXCEEDED_MAXIMUM_INPUT_ATTEMPTS);
	}
	
	public static long readLong(String prompt, int maxAttempts) {
		System.out.println(prompt);
		int attempts = 0;

		while (attempts < maxAttempts) {
			if (scanner.hasNextLong()) {
				return scanner.nextLong();
			} else {
				attempts++;
				System.out.println(
						"Invalid input. Please enter a valid number. Attempt " + attempts + " of " + maxAttempts);
				scanner.next();
			}
		}
		throw new IllegalArgumentException(Messages.EXCEEDED_MAXIMUM_INPUT_ATTEMPTS);
	}
}

