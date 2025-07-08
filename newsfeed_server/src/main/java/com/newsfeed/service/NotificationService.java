package com.newsfeed.service;

import com.newsfeed.dao.NotificationDao;
import com.newsfeed.dao.NotificationPrefernecesDao;
import com.newsfeed.dao.UserDao;
import com.newsfeed.exception.ServerException;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.model.NotificationHistory;
import com.newsfeed.model.User;
import com.newsfeed.util.EmailSender;
import com.newsfeed.util.IdGenerator;
import com.newsfeed.util.constants.Messages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotificationService {

	private NotificationDao notificationDao;
	private UserDao userDao;
	private NotificationPrefernecesDao notificationPreferencesDao;

	public NotificationService(NotificationDao notificationDao, UserDao userDao,
			NotificationPrefernecesDao notificationPreferencesDao) {
		this.notificationDao = notificationDao;
		this.userDao = userDao;
		this.notificationPreferencesDao = notificationPreferencesDao;
	}

	public void sendReportAlertToAdmins(String articleId, String reporterUserId) {
		List<User> adminUsers = userDao.getAllAdmins();
		String title = "An article was reported by user ID: " + reporterUserId;

		List<NotificationHistory> notifications = new ArrayList<>();
		for (User admin : adminUsers) {
			String message = "Hello " + admin.getName() + " ,\n An article was reported by user ID: " + reporterUserId
					+ ". The article Id '" + articleId + "' can be hidden from all the users.";

			NotificationHistory notification = new NotificationHistory();
			notification.setNotificationId(IdGenerator.generate("NOTIFY"));
			notification.setUserId(admin.getUserId());
			notification.setEmail(admin.getEmailAddress());
			notification.setMessage(message);
			notification.setArticleId(articleId);
			notification.setTitle(title);
			notification.setSentAt(LocalDateTime.now());
			notification.setRead(false);
			notifications.add(notification);
		}

		notificationDao.addNotifications(notifications);
	}

	public List<NotificationHistory> getNotificationsForUser(String userId) {
		return notificationDao.getNotificationsbyUser(userId);
	}

	public void markNotificationsAsRead(List<String> notificationIds) {
		notificationDao.markNotificationsAsRead(notificationIds);
	}

	public void generateNotifications(List<NewsArticle> articles) {
		try {
			Map<String, String> userEmails = userDao.getUsersWithEnabledPreferencesAndEmails();
			Map<String, List<String>> userPreferences = notificationPreferencesDao.getAllUserCategoryPreferences();

			List<NotificationHistory> notifications = new ArrayList<>();

			for (Map.Entry<String, String> entry : userEmails.entrySet()) {
				String userId = entry.getKey();
				String email = entry.getValue();

				List<String> preferredCategories = userPreferences.getOrDefault(userId, Collections.emptyList());

				for (NewsArticle article : articles) {
					if (preferredCategories.contains(article.getCategoryId())) {
						NotificationHistory notification = createArticleNotification(userId, article, email);
						notifications.add(notification);
					}
				}
			}

			if (!notifications.isEmpty()) {
				EmailSender.sendEmails(notifications, userEmails);
				notificationDao.addNotifications(notifications);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	private NotificationHistory createArticleNotification(String userId, NewsArticle article, String email) {
		NotificationHistory notification = new NotificationHistory();
		notification.setNotificationId(IdGenerator.generate("NOTIFY"));
		notification.setUserId(userId);
		notification.setArticleId(article.getArticleId());
		notification.setTitle(article.getTitle());
		notification.setMessage("New article in your preferred category: " + article.getTitle()
				+ ". Here is the article link: " + article.getArticleUrl());
		notification.setSentAt(LocalDateTime.now());
		notification.setEmail(email);
		notification.setRead(false);
		return notification;
	}

}
