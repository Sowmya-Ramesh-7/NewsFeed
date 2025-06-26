package com.newsfeed.controller;

import com.newsfeed.util.InputUtil;
import com.newsfeed.util.InputValidator;

import java.io.IOException;

import com.newsfeed.model.User;
import com.newsfeed.service.UserAuthenticationService;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Prompts;

public class UserAuthenticationController {
	private static final int MAXIMUM_INPUT_ATTEMPTS = 3;
	UserAuthenticationService authenticationService;

	public UserAuthenticationController(UserAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public boolean login(){
		String userId = InputUtil.readString(Prompts.ENTER_USER_ID);
		String password = InputUtil.readString(Prompts.ENTER_YOUR_PASSWORD);
		boolean isLoggedIn = authenticationService.login(userId, password);
		return isLoggedIn;
	}

	public void signup() throws IOException, InterruptedException {
		
		String name = InputUtil.readString(Prompts.ENTER_NAME);
		String email = InputUtil.readString(Prompts.ENTER_EMAIL);
		String password = InputUtil.readString(Prompts.ENTER_PASSWORD);
		Long phone = InputUtil.readLong(Prompts.ENTER_PHONE, MAXIMUM_INPUT_ATTEMPTS);
		String errorMessage = validateUserData(name, email, phone);
		
		if (!errorMessage.isBlank()) {
			throw new IllegalArgumentException(errorMessage);
		}
		
		User newUser = new User();
		newUser.setEmailAddress(email);
		newUser.setName(name);
		newUser.setPassword(password);
		newUser.setPhoneNumber(phone);
		
		authenticationService.signup(newUser);
	}

	private String validateUserData(String name, String email, Long phone) {
		String errorMessage = "";
		if (String.valueOf(phone).length() != 10) {
			errorMessage = Messages.INVALID_PHONE;
		}else if(!InputValidator.isPatternMatched(name, Patterns.NAME_PATTERN)) {
			errorMessage = Messages.INVALID_NAME;
		}else if(!InputValidator.isPatternMatched(email, Patterns.EMAIL_PATTERN)) {
			errorMessage = Messages.INVALID_EMAIL;
		}else if(!InputValidator.isPatternMatched(email, Patterns.EMAIL_PATTERN)) {
			errorMessage = Messages.INVALID_PASSWORD;
		}
		return errorMessage;
	}

	public void logout() {
		authenticationService.logout();
	}
}
