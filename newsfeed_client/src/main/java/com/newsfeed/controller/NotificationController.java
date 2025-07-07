package com.newsfeed.controller;

import com.newsfeed.model.NotificationHistory;
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
	
	public NotificationController(NotificationService notificationService){
		this.notificationService = notificationService;
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

	private void configureNotifications() throws IOException {
		Map<String, Boolean> preferences = notificationService.getCurrentConfig();
		while (true) {
			System.out.println("\nCONFIGURE - NOTIFICATIONS");
			int index = 1;
			for (Map.Entry<String, Boolean> entry : preferences.entrySet()) {
				System.out.printf("%d. %s - %s\n", index++, entry.getKey(), entry.getValue() ? "Enabled" : "Disabled");
			}
			System.out.println(index + ". Back");

			String input = InputUtil.readLine(Prompts.ENTER_YOUR_CHOICE);
			try {
				int option = Integer.parseInt(input);
				if (option == index)
					return;

				String category = preferences.keySet().toArray(new String[0])[option - 1];
				boolean current = preferences.get(category);

				if ("Keywords".equalsIgnoreCase(category)) {
					String newKeywords = InputUtil.readLine(Prompts.ENTER_KEYWORDS_FOR_NOTIFICATION);
					notificationService.setKeywordPreferences(newKeywords);
					preferences.put(category, true);
				} else {
					preferences.put(category, !current);
					notificationService.setCategoryPreference(category, !current);
				}
			} catch (Exception e) {
				System.out.println(Messages.INVALID_OPTION);
			}
		}
	}
}
