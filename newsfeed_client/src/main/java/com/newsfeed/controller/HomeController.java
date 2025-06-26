package com.newsfeed.controller;

import java.io.IOException;
import java.util.Scanner;

import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.*;

public class HomeController {
	
	public static boolean home(Scanner scanner) {
		UserAuthenticationController authenticationController = ApplicationContext.getObject(UserAuthenticationController.class);
		boolean isExit = false;
		try {
			System.out.println("\n" + Messages.WELCOME);
			System.out.println("\n" +Messages.SIGNUP_OPTION);
			System.out.println("\n" +Messages.ADMIN_SIGNUP_OPTION);
			System.out.println("\n" +Messages.LOGIN_OPTION);
			System.out.println("\n" +Messages.EXIT_OPTION);
			System.out.print("\n" +Prompts.CHOOSE_OPTION);

			String choice = scanner.nextLine();

			switch (choice) {
			case "1":
				authenticationController.signup();
				break;
			case "2":
				boolean isLoggedIn = authenticationController.login();
				if (isLoggedIn) {
					//showUserMenu(scanner);
					System.out.println();
				}
				break;
			case "3":
				System.out.println(Messages.EXIT_MESSAGE);
				isExit = true;
				break;
			default:
				System.out.println(Messages.INVALID_OPTION);
			}
		} catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
			String isContinue = InputUtil.readString(Prompts.DO_YOU_WANT_TO_CONTIUE);
			isExit = !isContinue.equalsIgnoreCase("YES");
		} catch (IOException | InterruptedException exception) {
			System.err.println(exception.getMessage());
		} 
		return isExit;
	}
}