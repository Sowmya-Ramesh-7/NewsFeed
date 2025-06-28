package com.newsfeed.controller;

import com.newsfeed.util.InputUtil;

import java.io.IOException;

import com.newsfeed.model.User;
import com.newsfeed.service.UserAuthenticationService;
import com.newsfeed.util.constants.Constants;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Prompts;

public class UserAuthenticationController {
	private final UserAuthenticationService authenticationService;

	public UserAuthenticationController(UserAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public boolean login() throws IOException, InterruptedException {
		try {
			String email = InputUtil.readLine(Prompts.ENTER_EMAIL, Patterns.EMAIL_PATTERN, Messages.INVALID_EMAIL);
			String password = InputUtil.readLine(Prompts.ENTER_YOUR_PASSWORD);
			return authenticationService.login(email, password);
		}catch(IllegalArgumentException exception){
			System.out.println(Messages.LOGIN_FAILED + exception.getMessage());
			return false;
		}
	}

	public void signup() throws IOException, InterruptedException {
		try {
			String name = InputUtil.readLine(Prompts.ENTER_NAME, Patterns.NAME_PATTERN, Messages.INVALID_NAME);
			String email = InputUtil.readLine(Prompts.ENTER_EMAIL, Patterns.EMAIL_PATTERN, Messages.INVALID_EMAIL);
			String password = InputUtil.readLine(Prompts.ENTER_PASSWORD, Patterns.PASSWORD_PATTERN, Messages.INVALID_PASSWORD);
			long phone = getPhoneNumber();

			User newUser = new User();
			newUser.setName(name);
			newUser.setEmailAddress(email);
			newUser.setPassword(password);
			newUser.setPhoneNumber(phone);

			authenticationService.signup(newUser);
		}catch(IllegalArgumentException exception){
			System.out.println(Messages.SIGNUP_FAILED + exception.getMessage());
		}
	}

	public void logout() {
		authenticationService.logout();
	}

	private long getPhoneNumber() {
		int attempts = 0;
		while (attempts < Constants.MAXIMUM_INPUT_ATTEMPTS) {
			long phone = InputUtil.readLong(Prompts.ENTER_PHONE);
			if (String.valueOf(phone).length() == 10) {
				return phone;
			}
			attempts++;
			System.out.println(Messages.INVALID_PHONE + Messages.PLEASE_TRY_AGAIN);
		}
		throw new IllegalArgumentException(Messages.EXCEEDED_MAXIMUM_INPUT_ATTEMPTS);
	}
}