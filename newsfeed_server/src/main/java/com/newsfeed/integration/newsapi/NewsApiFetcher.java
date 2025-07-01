package com.newsfeed.integration.newsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.integration.NewsFetcher;
import com.newsfeed.model.ExternalServer;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.ArticleCategorizer;
import com.newsfeed.service.NewsArticleService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.IdGenerator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewsApiFetcher implements NewsFetcher {

	private static final HttpClient httpClient = ApplicationContext.getObject(HttpClient.class);
	private static final ObjectMapper objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	private static final NewsArticleService articleService = ApplicationContext.getObject(NewsArticleService.class);
	private static final ArticleCategorizer newsArticleClassifier = ApplicationContext.getObject(ArticleCategorizer.class);
	
	private static final String SOURCE = "abc-news";

	@Override
	public List<NewsArticle> fetchArticles(ExternalServer server) throws IOException, InterruptedException {
		String fromDate = server.getLastAccessed().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String apiUrl = server.getBaseUrl() + "?from=" + fromDate + "&sources=" + SOURCE +"&language=en";
		String apiKey = server.getApiKey();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).header("X-Api-Key", apiKey)
				.header("Accept", "application/json").GET().build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			System.err.println("NewsApi Error: " + response.body());
			return new ArrayList<>();
		}

		NewsApiResponse newsApiResponse = objectMapper.readValue(response.body(), NewsApiResponse.class);
		List<NewsArticle> articles = newsApiResponse.getArticles();
		articles.forEach(article -> article.setArticleId(IdGenerator.generate("Article")));
		newsArticleClassifier.categorize(articles);
		return articles;
	}

	@Override
	public void saveArticles(List<NewsArticle> newsArticles) {
		articleService.addAll(newsArticles);
	}

}
