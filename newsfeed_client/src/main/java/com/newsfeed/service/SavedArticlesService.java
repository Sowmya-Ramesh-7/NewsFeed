package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.ApiRoutes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SavedArticlesService {

	private final HttpClient httpClient = HttpClient.newHttpClient();
	private final ObjectMapper objectMapper = ApplicationContext.getObject(ObjectMapper.class);

	public boolean saveArticle(String articleId) throws IOException, InterruptedException {
		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
		String requestBody = objectMapper.writeValueAsString(Map.of("articleId", articleId));

		HttpRequest request = HttpRequestBuilder.buildRequest("POST", ApiRoutes.SAVED_ARTICLE, headers, requestBody);

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

		System.out.println(apiResponse.getMessage());
		return apiResponse.isSuccess();

	}

	public List<NewsArticle> getSavedArticles() throws IOException, InterruptedException {
		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
		HttpRequest request = HttpRequestBuilder.buildRequest("GET", ApiRoutes.SAVED_ARTICLE, headers);

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		System.out.println(apiResponse.getMessage());
		if (response.statusCode() == 200) {
			return objectMapper.convertValue(apiResponse.getData(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, NewsArticle.class));
		}

		return Collections.emptyList();
	}
}
