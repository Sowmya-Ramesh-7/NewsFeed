package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.util.constants.ApiRoutes;

public class NewsArticleService {
	private ObjectMapper objectMapper;
	private HttpClient httpClient;

	public NewsArticleService(ObjectMapper objectMapper, HttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.httpClient = httpClient;
	}

	public List<NewsArticle> getArticlesForToday() throws IOException, InterruptedException {
		String today = LocalDate.now().toString();
		String url = ApiRoutes.ARTICLES_ROUTE + "?start=" + today + "&end=" + today;
		return getArticles(url);
	}

	public List<NewsArticle> getArticlesByDateRange(LocalDate startDate, LocalDate endDate)
			throws IOException, InterruptedException {
		String url = ApiRoutes.ARTICLES_ROUTE + "?start=" + startDate + "&end=" + endDate;
		return getArticles(url);
	}

	public List<NewsArticle> getArticlesByCategory(String categoryId) throws IOException, InterruptedException {
		String url = ApiRoutes.ARTICLES_ROUTE + "?category=" + categoryId;
		return getArticles(url);
	}

	public List<NewsArticle> getArticlesByText(String keyword) throws IOException, InterruptedException {
		String url = ApiRoutes.ARTICLES_ROUTE + "?q=" + keyword;
		return getArticles(url);
	}

	public List<NewsArticle> getArticlesByFilters(LocalDate startDate, LocalDate endDate, String categoryId)
			throws IOException, InterruptedException {
		String url = ApiRoutes.ARTICLES_ROUTE + "?start=" + startDate + "&end=" + endDate + "&category=" + categoryId;
		return getArticles(url);
	}

	private List<NewsArticle> getArticles(String url) throws IOException, InterruptedException {
		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
		HttpRequest request = HttpRequestBuilder.buildRequest("GET", url, headers);
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		System.out.println(apiResponse.getMessage());
		if (apiResponse.isSuccess()) {
			Object data = apiResponse.getData();
			return objectMapper.convertValue(data, new TypeReference<List<NewsArticle>>() {
			});
		} else {
			return Collections.emptyList();
		}
	}

	public void hideArticleById(String articleId) throws IOException, InterruptedException {
		Map<String, String> requestBody = new HashMap<String, String>();
		requestBody.put("articleId", articleId);
		sendPutRequest(requestBody);
	}

	public void hideArticlesByCategory(String categoryId) throws IOException, InterruptedException {
		Map<String, String> requestBody = new HashMap<String, String>();
		requestBody.put("categoryId", categoryId);
		sendPutRequest(requestBody);
	}

	public void hideArticlesByKeyword(String keyword) throws IOException, InterruptedException {
		Map<String, String> requestBody = new HashMap<String, String>();
		requestBody.put("keyword", keyword);
		sendPutRequest(requestBody);
	}

	private void sendPutRequest(Map<String, String> requestBody) throws IOException, InterruptedException {
		String json = objectMapper.writeValueAsString(requestBody);
		HttpRequest request = HttpRequestBuilder.buildRequest("PUT", ApiRoutes.ARTICLES_ROUTE, HttpRequestBuilder.getAuthHeader(), json);

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		System.out.println(apiResponse.getMessage());
		
		
	}
}
