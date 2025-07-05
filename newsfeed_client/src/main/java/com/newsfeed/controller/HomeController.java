package com.newsfeed.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.newsfeed.model.NewsArticle;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Patterns;
import com.newsfeed.util.constants.Prompts;
import com.newsfeed.util.enums.AdminMenu;
import com.newsfeed.util.enums.MainMenu;
import com.newsfeed.util.enums.UserMenu;

public class HomeController {

	private static final UserAuthenticationController authenticationController = ApplicationContext.getObject(UserAuthenticationController.class);
	private static final NewsArticlesController newsArticlesController = ApplicationContext.getObject(NewsArticlesController.class);
	private static final SavedArticlesController savedArticlesController = ApplicationContext.getObject(SavedArticlesController.class);
	private static final NotificationController notificationController = ApplicationContext.getObject(NotificationController.class);
	private static final ExternalServerController externalServerController = ApplicationContext.getObject(ExternalServerController.class);
	private static final NewsCategoryController categoryController = ApplicationContext.getObject(NewsCategoryController.class);
	private static final CustomisedNewsFeedController customisedNewsFeedController = ApplicationContext.getObject(CustomisedNewsFeedController.class);

	public static boolean home() {
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
					handleLogin();
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

	private static void handleLogin() throws IOException, InterruptedException {
		System.out.println("\n" + Messages.LOGIN_HEADER);
		Map<String, String> loginResponse = authenticationController.login();
		while (authenticationController.isLoggedIn()) {
			boolean isAdmin = loginResponse.containsKey("isAdmin") ? 
					Boolean.parseBoolean(loginResponse.get("isAdmin"))
					: false;

			if (isAdmin) {
				System.out.println("\n" + Messages.ADMIN_MENU_HEADER);
				showAdminMenu();
			} else {
				String date = LocalDate.now().format(DateTimeFormatter.ofPattern(Patterns.DATE_FORMAT));
				String time = LocalTime.now().withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern(Patterns.TIME_FORMAT));

				System.out.println("\n" + String.format(Messages.USER_MENU_HEADER, loginResponse.get("name"), date, time));
				showUserMenu();
			}
		}
	}

	private static void showAdminMenu() throws IOException, InterruptedException {
		try {
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
					break;
			}
		} catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
		}
	}

	private static void showUserMenu() throws IOException, InterruptedException {
		try {
			System.out.println(Prompts.PLEASE_CHOOSE_OPTION);
			for (UserMenu option : UserMenu.values()) {
				System.out.println(option.getOptionNumber() + ". " + option.getDescription());
			}

			String choice = InputUtil.readLine(Prompts.ENTER_YOUR_CHOICE);
			UserMenu selectedOption = UserMenu.getByOptionNumber(choice);

			switch (selectedOption) {
				case HEADLINES:
					showHeadlinesMenu();
					break;
				case SAVED_ARTICLES:
					savedArticlesController.showSavedArticles();
					break;
				case SEARCH:
					newsArticlesController.getArticlesByDateRange();
					break;
				case NOTIFICATIONS:
					notificationController.manageNotifications();
					break;
				case LOGOUT:
					authenticationController.logout();
					break;
			}
		} catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
		}
	}

	private static void showHeadlinesMenu() throws IOException, InterruptedException {
		System.out.println(Messages.HEADLINES_MENU_HEADER);
		System.out.println("1. Today");
		System.out.println("2. Date range");
		System.out.println("3. Logout");
		List<NewsArticle> newsArticles;
		String option = InputUtil.readLine(Prompts.CHOOSE_OPTION);
		switch (option) {
			case "1":
				newsArticles = newsArticlesController.getArticlesForToday();
				displayNewsArticles(newsArticles);
				break;
			case "2":
				newsArticles = newsArticlesController.getArticlesByDateRange();
				displayNewsArticles(newsArticles);
				break;
			case "3":
				authenticationController.logout();
				break;
			default:
				System.out.println(Messages.INVALID_OPTION);
		}
	}
	
	private static void displayNewsArticles(List<NewsArticle> newsArticles) throws IOException, InterruptedException {
		if (newsArticles == null || newsArticles.isEmpty()) {
			return;
		}
		int index = 0;

		while (index >= 0 && index < newsArticles.size()) {
			NewsArticle newsArticle = newsArticles.get(index);
			boolean goToPreviousMenu = false;

			System.out.println(Messages.LINE);
			System.out.println("Article " + (index + 1) + " of " + newsArticles.size());
			System.out.println(Messages.LINE);

			displayArticlesMenu();
			
			System.out.println(newsArticle.toString());
			
			String choice = InputUtil.readLine(Prompts.CHOOSE_OPTION);

			switch (choice) {
				case "1":
					goToPreviousMenu = true;
					break;
				case "2":
					authenticationController.logout();
					goToPreviousMenu = true;
					break;
				case "3":
					savedArticlesController.save();
					break;
				case "4":
					customisedNewsFeedController.likeArticle();
					break;
				case "5":
					customisedNewsFeedController.disLikeArticle();
					break;
				case "6":
					customisedNewsFeedController.reportArticle();
					break;
				case "7":
					if (index < newsArticles.size() - 1) {
						index++;
					} else {
						System.out.println(Messages.LAST_ARTICLE);
					}
					break;
				case "8":
					if (index > 0) {
						index--;
					} else {
						System.out.println(Messages.FIRST_ARTICLE);
					}
					break;
				default:
					System.out.println(Messages.INVALID_OPTION);
			}

			if (goToPreviousMenu) {
				break;
			}
		}
	}

	private static void displayArticlesMenu() {
		System.out.println("\n1. Back to Menu");
		System.out.println("2. Logout");
		System.out.println("3. Save Article");
		System.out.println("4. Like");
		System.out.println("5. Dislike");
		System.out.println("6. Report");
		System.out.println("7. Next");
		System.out.println("8. Previous");
	}
				
}
