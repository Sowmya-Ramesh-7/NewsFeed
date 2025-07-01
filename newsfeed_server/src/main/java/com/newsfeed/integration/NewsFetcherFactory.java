package com.newsfeed.integration;

import com.newsfeed.integration.newsapi.NewsApiFetcher;
import com.newsfeed.integration.thenewsapi.TheNewsApiFetcher;
import com.newsfeed.model.ExternalServer;
import com.newsfeed.util.ApplicationContext;

public class NewsFetcherFactory {

	public static NewsFetcher getFetcher(ExternalServer server) {
		return switch (server.getApiName()) {
			case "NewsApi" -> ApplicationContext.getObject(NewsApiFetcher.class);
			case "TheNewsApi" -> ApplicationContext.getObject(TheNewsApiFetcher.class);
			default -> throw new IllegalArgumentException("Unsupported API: " + server.getApiName());
		};
	}
}
