package com.newsfeed.util.constants;

public class Patterns {
	public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]{1}[a-zA-Z0-9]+[.]{1}[a-zA-Z0-9]+$";
	public static final String NAME_PATTERN = "^(?!\\s*$)[a-zA-Z\\s]+$";
	public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
}
