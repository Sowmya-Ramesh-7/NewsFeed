package com.newsfeed.integration;

import java.io.IOException;
import java.util.List;

import com.newsfeed.model.ExternalServer;
import com.newsfeed.model.NewsArticle;

public interface NewsFetcher {
	List<NewsArticle> fetchArticles(ExternalServer server) throws IOException, InterruptedException;
	void saveArticles(List<NewsArticle> newsArticles);
}
