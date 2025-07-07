package com.newsfeed.service;

import com.newsfeed.dao.NotificationDao;
import com.newsfeed.dao.UserDao;
import com.newsfeed.model.NotificationHistory;
import com.newsfeed.model.User;
import com.newsfeed.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

	private NotificationDao notificationDao;
	private UserDao userDao;

	public NotificationService(NotificationDao notificationDao, UserDao userDao) {
		this.notificationDao = notificationDao;
		this.userDao = userDao;
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

}
