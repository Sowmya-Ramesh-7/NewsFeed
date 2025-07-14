package com.newsfeed.controller;

import com.newsfeed.model.NewsCategory;
import com.newsfeed.model.NotificationHistory;
import com.newsfeed.service.NewsCategoryService;
import com.newsfeed.service.NotificationPreferencesService;
import com.newsfeed.service.NotificationService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Prompts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationController {

	private NotificationService notificationService;
	private NotificationPreferencesService notificationPreferencesService;
	private NewsCategoryService newsCategoryService;

	public NotificationController(NotificationService notificationService, NotificationPreferencesService notificationPreferencesService, NewsCategoryService newsCategoryService) {
		this.notificationService = notificationService;
		this.notificationPreferencesService = notificationPreferencesService;
		this.newsCategoryService = newsCategoryService;
	}

	public void manageNotifications() throws IOException, InterruptedException {
		while (true) {
			System.out.println("\n" + Messages.NOTIFICATION_MENU_HEADER);
			System.out.println("1. View Notifications");
			System.out.println("2. Configure Notifications");
			System.out.println("3. Back");
			System.out.println("4. Logout");

			String choice = InputUtil.readLine(Prompts.ENTER_YOUR_CHOICE);
			switch (choice) {
			case "1":
				viewNotifications();
				break;
			case "2":
				configureNotifications();
				break;
			case "3":
				return;
			case "4":
				ApplicationContext.getObject(UserAuthenticationController.class).logout();
				return;
			default:
				System.out.println(Messages.INVALID_OPTION);
			}
		}
	}

	public void viewNotifications() throws IOException, InterruptedException {
		List<NotificationHistory> notifications = notificationService.getUserNotifications();

		if (notifications.isEmpty()) {
			return;
		}

		List<String> readIds = new ArrayList<String>();
		for (NotificationHistory notification : notifications) {
			System.out.println("\nTitle: " + notification.getTitle());
			System.out.println("Message: " + notification.getMessage());
			System.out.println("Sent At: " + notification.getSentAt());
			System.out.println(Messages.LINE);

			readIds.add(notification.getNotificationId());

			String choice = InputUtil.readLine("1. Back\n2. Next\nEnter your choice: ");
			if (choice.equals("1")) {
				break;
			}
		}

		if (!readIds.isEmpty()) {
			notificationService.markNotificationsAsRead(readIds);
		}
	}

	private void configureNotifications() throws IOException, InterruptedException {
		Map<String, Boolean> preferences = notificationPreferencesService.getCategoryPreferences();
		Map<String, NewsCategory> allCategories = newsCategoryService.getAllCategories();

		List<String> categoryIds = new ArrayList<>(preferences.keySet());

		while (true) {
			System.out.println(Messages.CONFIGURE_NOTIFICATIONS_HEADER);
			for (int i = 0; i < categoryIds.size(); i++) {
				String categoryId = categoryIds.get(i);
				String categoryName = allCategories.containsKey(categoryId)
						? allCategories.get(categoryId).getCategory()
						: categoryId;
				boolean isEnabled = preferences.get(categoryId);
				System.out.printf("%d. %s - %s\n", i + 1, categoryName, isEnabled ? "Enabled" : "Disabled");
			}
			System.out.printf("%d. Back\n", categoryIds.size() + 1);

			String input = InputUtil.readLine(Prompts.SELECT_CATEGORY_TO_ENABLE_OR_DISABLE);
			int option = Integer.parseInt(input);
			if (option == categoryIds.size() + 1) return;

			String selectedCategoryId = categoryIds.get(option - 1);
			boolean currentValue = preferences.get(selectedCategoryId);
			preferences.put(selectedCategoryId, !currentValue);
			notificationPreferencesService.upsertCategoryPreference(selectedCategoryId, !currentValue);
		}
	}

}
