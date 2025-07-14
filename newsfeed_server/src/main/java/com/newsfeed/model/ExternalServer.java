package com.newsfeed.model;

import java.time.LocalDateTime;

public class ExternalServer {
	private int serverId;
	private String apiName;
	private String baseUrl;
	private String apiKey;
	private LocalDateTime lastAccessed;
	private boolean isActive;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public LocalDateTime getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(LocalDateTime lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
