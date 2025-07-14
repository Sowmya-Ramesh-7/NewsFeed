package com.newsfeed.dao;

import com.newsfeed.model.NotificationHistory;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;
import com.newsfeed.exception.ServerException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationDao {

	public void addNotifications(List<NotificationHistory> notifications) {
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.INSERT_NOTIFICATION)) {

			for (NotificationHistory notification : notifications) {
				preparedStatement.setString(1, notification.getNotificationId());
				preparedStatement.setString(2, notification.getUserId());
				preparedStatement.setString(3, notification.getMessage());
				preparedStatement.setString(4, notification.getArticleId());
				preparedStatement.setString(5, notification.getTitle());
				preparedStatement.setObject(6, notification.getSentAt());
				preparedStatement.setBoolean(7, notification.isRead());
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	public List<NotificationHistory> getNotificationsbyUser(String userId) {
		String query = Query.GET_UNREAD_NOTIFICATION_FOR_USER;
		List<NotificationHistory> notifications = new ArrayList<>();
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				NotificationHistory notification = new NotificationHistory();
				notification.setNotificationId(resultSet.getString("notification_id"));
				notification.setUserId(resultSet.getString("user_id"));
				notification.setMessage(resultSet.getString("message"));
				notification.setArticleId(resultSet.getString("article_id"));
				notification.setTitle(resultSet.getString("title"));
				notification.setSentAt(resultSet.getTimestamp("sent_at").toLocalDateTime());
				notification.setRead(resultSet.getBoolean("is_read"));
				notifications.add(notification);
			}
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
		return notifications;
	}

	public void markNotificationsAsRead(List<String> notificationIds) {
		if (notificationIds == null || notificationIds.isEmpty())
			return;

		String placeholders = String.join(",", Collections.nCopies(notificationIds.size(), "?"));
		String query = "UPDATE notification_history SET is_read = TRUE WHERE notification_id IN (" + placeholders + ")";

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			for (int i = 0; i < notificationIds.size(); i++) {
				statement.setString(i + 1, notificationIds.get(i));
			}

			statement.executeUpdate();

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

}
