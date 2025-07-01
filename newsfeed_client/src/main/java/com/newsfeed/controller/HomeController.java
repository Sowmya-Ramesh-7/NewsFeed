package com.newsfeed.controller;

import java.io.IOException;
import java.util.Map;

import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Prompts;
import com.newsfeed.util.enums.AdminMenu;
import com.newsfeed.util.enums.MainMenu;

public class HomeController {

	public static boolean home() {
		UserAuthenticationController authenticationController = ApplicationContext.getObject(UserAuthenticationController.class);
		boolean isExit = false;

		try {
			System.out.println("\n" + Messages.WELCOME);
			for (MainMenu option : MainMenu.values()) {
				System.out.println(option.getOptionNumber() + ". " + option.getDescription());
			}
			String choice = InputUtil.readLine(Prompts.CHOOSE_OPTION);

			MainMenu selectedOption = MainMenu.getByOptionNumber(choice);

			switch (selectedOption) {
			case SIGNUP:
				System.out.println("\n" + Messages.SIGNUP_HEADER);
				authenticationController.signup();
				break;

			case LOGIN:
				System.out.println("\n" + Messages.LOGIN_HEADER);
				Map<String, String> loginResponse = authenticationController.login();
				boolean isLoggedIn = loginResponse.containsKey("token") && !loginResponse.get("token").isBlank();
				while (isLoggedIn) {
					boolean isAdmin = loginResponse.containsKey("isAdmin")
							? Boolean.parseBoolean(loginResponse.get("isAdmin"))
							: false;

					if (isAdmin) {
						isLoggedIn = showAdminMenu(authenticationController);
					} else {
						System.out.println("user");
						// isLoggedIn = showUserMenu(authenticationController);
					}
				}
				break;

			case EXIT:
				System.out.println(Messages.EXIT_MESSAGE);
				isExit = true;
				break;
			}

		} catch (IllegalArgumentException | IOException | InterruptedException exception) {
			System.out.println(exception.getMessage());
		}

		return isExit;
	}

	private static boolean showAdminMenu(UserAuthenticationController authenticationController)
			throws IOException, InterruptedException {
		ExternalServerController externalServerController = ApplicationContext
				.getObject(ExternalServerController.class);
		NewsCategoryController categoryController = ApplicationContext.getObject(NewsCategoryController.class);

		try {
			System.out.println("\n" + Messages.ADMIN_MENU_HEADER);
			for (AdminMenu option : AdminMenu.values()) {
				System.out.println(option.getOptionNumber() + ". " + option.getDescription());
			}
			String choice = InputUtil.readLine(Prompts.CHOOSE_OPTION);
			AdminMenu selectedOption = AdminMenu.getByOptionNumber(choice);

			switch (selectedOption) {
			case VIEW_ALL_SERVER_STATUS:
				externalServerController.getAllServersWithStatus();
				break;

			case VIEW_ALL_SERVER_DETAILS:
				externalServerController.getAllServerDetails();
				break;

			case EDIT_SERVER_DETAILS:
				externalServerController.updateServerDetails();
				break;

			case ADD_CATEGORY:
				categoryController.addNewsCategory();
				break;

			case LOGOUT:
				authenticationController.logout();
				return false;
			}
		} catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
		}
		return true;
	}
}
