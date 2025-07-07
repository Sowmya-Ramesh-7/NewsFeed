package com.newsfeed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserNotificationPreference{
	private String newsCategoryId;
	private boolean isEnabled;
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonAlias("categoryId")
	public String getNewsCategoryId() {
		return newsCategoryId;
	}

	public void setNewsCategoryId(String newsCategoryId) {
		this.newsCategoryId = newsCategoryId;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}
