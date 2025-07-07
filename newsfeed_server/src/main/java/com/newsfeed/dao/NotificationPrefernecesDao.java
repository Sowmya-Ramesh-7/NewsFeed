package com.newsfeed.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newsfeed.exception.ServerException;
import com.newsfeed.model.UserNotificationPreference;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

public class NotificationPrefernecesDao {
	
	public void addNotificationPreferences(List<UserNotificationPreference> userNotificationPreferences) {
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.INSERT_NOTIFICATION_PREFERENCES)) {

			for (UserNotificationPreference notificationPreference : userNotificationPreferences) {
				preparedStatement.setString(1, notificationPreference.getUserId());
				preparedStatement.setString(2, notificationPreference.getNewsCategoryId());
				preparedStatement.setBoolean(3, notificationPreference.getIsEnabled());
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	public void upsertCategoryPreference(UserNotificationPreference userNotificationPreference) {
	    String query = Query.UPSERT_CATEGORY_NOTIFICATION_PREFERENCES;

	    try (Connection connection = DBConnect.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setString(1, userNotificationPreference.getUserId());
	        preparedStatement.setString(2, userNotificationPreference.getNewsCategoryId());
	        preparedStatement.setBoolean(3, userNotificationPreference.getIsEnabled());
	        preparedStatement.executeUpdate();

	    } catch (SQLException | ClassNotFoundException | IOException exception) {
	    	exception.printStackTrace();
	        throw new ServerException(Messages.DATABASE_ERROR);
	    }
	}
	
	public Map<String, Boolean> getNotificationPreferences(String userId) {
	    Map<String, Boolean> preferences = new HashMap<String, Boolean>();

	    try (Connection connection = DBConnect.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(Query.GET_CATEGORY_PREFERENCES_BY_USER_ID)) {

	    	preparedStatement.setString(1, userId);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            String categoryId = resultSet.getString("category_id");
	            boolean isEnabled = resultSet.getBoolean("is_enabled");
	            preferences.put(categoryId, isEnabled);
	        }

	    } catch (SQLException | ClassNotFoundException | IOException exception) {
	    	exception.printStackTrace();
	        throw new ServerException(Messages.DATABASE_ERROR);
	    }
	    return preferences;
	}

	public Map<String, List<String>> getAllUserCategoryPreferences() {
	    Map<String, List<String>> userPreferencesMap = new HashMap<>();

	    String query = Query.GET_ENABLED_NOTIFICATION_CATEGORY_FOR_USER;

	    try (Connection connection = DBConnect.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            String userId = resultSet.getString("user_id");
	            String categoryId = resultSet.getString("category_id");

	            userPreferencesMap.computeIfAbsent(userId, key -> new ArrayList<>()).add(categoryId);
	        }

	    } catch (SQLException | IOException | ClassNotFoundException exception) {
	        exception.printStackTrace();
	        throw new ServerException(Messages.DATABASE_ERROR);
	    }

	    return userPreferencesMap;
	}

}
