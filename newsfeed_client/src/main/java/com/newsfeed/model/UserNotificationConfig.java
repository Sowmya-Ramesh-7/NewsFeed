package com.newsfeed.model;

import java.util.List;

public class UserNotificationConfig {
    private String notificationConfigId;
    private boolean isEmailNotificationEnabled;
    private List<String> newsCategory;
    private String userId;
    
	public String getNotificationConfigId() {
		return notificationConfigId;
	}
	
	public void setNotificationConfigId(String notificationConfigId) {
		this.notificationConfigId = notificationConfigId;
	}

	public boolean isEmailNotificationEnabled() {
		return isEmailNotificationEnabled;
	}

	public void setEmailNotificationEnabled(boolean isEmailNotificationEnabled) {
		this.isEmailNotificationEnabled = isEmailNotificationEnabled;
	}

	public List<String> getNewsCategory() {
		return newsCategory;
	}

	public void setNewsCategory(List<String> newsCategory) {
		this.newsCategory = newsCategory;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
}
