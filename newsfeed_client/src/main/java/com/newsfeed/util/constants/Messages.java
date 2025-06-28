package com.newsfeed.util.constants;

public class Messages {
	private static final String BLACK_BACKGROUND = "\u001B[40m";
	private static final String WHITE_TEXT= "\u001B[37m";
	private static final String RESET_TO_BLACK_TEXT = "\u001B[0m";
	private static final String HIGHLIGHT_TEXT = BLACK_BACKGROUND + WHITE_TEXT + "%s" + RESET_TO_BLACK_TEXT;
	public static final String WELCOME = String.format(HIGHLIGHT_TEXT, "----------------Welcome to News Feed Application---------------");
	public static final String SIGNUP_OPTION = "1. Signup";
	public static final String LOGIN_OPTION = "2. Login";
	public static final String EXIT_OPTION = "3. Exit";
	public static final String EXCEEDED_MAXIMUM_INPUT_ATTEMPTS = "Exceeded maximum input attempts.";
	public static final String LOGIN_SUCCESSFUL = "Login Successful";
	public static final String EXIT_MESSAGE = "Newsfeed Application Closed. Please visit Again";
	public static final String INVALID_OPTION = "Invalid Option selected";
	public static final String INVALID_PHONE = "Phone number should have 10 digits.";
	public static final String UNSUPPORTED_HTTP_METHOD = "Unsupported HTTP method: %s";
	public static final String INVALID_EMAIL = "Invalid email format.";
	public static final String INVALID_NAME = "Name should have only alphabets and space.";
	public static final String INVALID_PASSWORD = "Password should be atleast 8 characters and have one capital letter, small letter, number and special character.";
	public static final String PLEASE_TRY_AGAIN = " Please try again.";
	public static final String INVALID_NUMBER = "Invalid input. Please enter a valid number.";
	public static final String SIGNUP_FAILED = "Signup Failed! ";
	public static final String LOGIN_FAILED = "Login Failed! ";
	public static final String SIGNUP_HEADER = String.format(HIGHLIGHT_TEXT, "--------------- Please your details for Signup: ---------------");
	

}
