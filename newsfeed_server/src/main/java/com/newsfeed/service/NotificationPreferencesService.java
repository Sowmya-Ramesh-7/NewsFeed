package com.newsfeed.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.newsfeed.dao.NewsCategoryDao;
import com.newsfeed.dao.NotificationPrefernecesDao;
import com.newsfeed.model.UserNotificationPreference;

public class NotificationPreferencesService {
	private NotificationPrefernecesDao notificationPrefernecesDao;
	private NewsCategoryDao categoryDao;
	
	public NotificationPreferencesService(NotificationPrefernecesDao notificationPrefernecesDao, NewsCategoryDao categoryDao){
		this.notificationPrefernecesDao = notificationPrefernecesDao;
		this.categoryDao = categoryDao;
	}

	public void setDefaultPreferences(String userId) {
		List<UserNotificationPreference> userNotificationPreferences = new ArrayList<UserNotificationPreference>();
		Set<String> categorieIds = categoryDao.getAllCategories().keySet();
		for(String categoryId: categorieIds) {
			UserNotificationPreference userNotificationPreference = new UserNotificationPreference();
			userNotificationPreference.setUserId(userId);
			userNotificationPreference.setNewsCategoryId(categoryId);
			userNotificationPreference.setIsEnabled(false);
			userNotificationPreferences.add(userNotificationPreference);
		}
		notificationPrefernecesDao.addNotificationPreferences(userNotificationPreferences);
	}

	public Map<String, Boolean> getNotificationPreferences(String userId) {
		return notificationPrefernecesDao.getNotificationPreferences(userId);
	}

	public void setCategoryPreference(UserNotificationPreference userNotificationPreference) {
		notificationPrefernecesDao.upsertCategoryPreference(userNotificationPreference);
	}

	

}
