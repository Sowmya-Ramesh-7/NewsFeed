package com.newsfeed.integration;

import com.newsfeed.model.ExternalServer;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.ExternalServerService;
import com.newsfeed.service.NotificationService;
import com.newsfeed.util.ApplicationContext;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThirdPartyNewsScheduler {

	private static final ScheduledExecutorService scheduler = ApplicationContext
			.getObject(ScheduledExecutorService.class);
	
	private final ExternalServerService serverService;
	private final NotificationService notificationService;

	public ThirdPartyNewsScheduler(ExternalServerService serverService, NotificationService notificationService) {
		this.serverService = serverService;
		this.notificationService = notificationService;
	}

	public void scheduleNewsFetchTasks() {
		scheduler.scheduleAtFixedRate(this::fetchFromAllSources, 0, 3, TimeUnit.HOURS);
	}

	private void fetchFromAllSources() {
		List<ExternalServer> servers = serverService.findAll();

		for (ExternalServer server : servers) {
			if (!server.isActive()) {
				continue;
			}

			NewsFetcher fetcher = NewsFetcherFactory.getFetcher(server);

			try {
				List<NewsArticle> articles = fetcher.fetchArticles(server);
				
				fetcher.saveArticles(articles);
				notificationService.generateNotifications(articles);
				serverService.updateLastAccessed(server.getServerId());
			} catch (Exception excetpion) {
				excetpion.printStackTrace();
				System.err.println("Failed to fetch from " + server.getApiName() + ": " + excetpion.getMessage());
			}
		}
		
	}
}
