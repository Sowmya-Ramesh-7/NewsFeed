package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.util.constants.ApiRoutes;

public class ArticleHistoryService {

	private ObjectMapper objectMapper;
	private HttpClient httpClient;
	
	public ArticleHistoryService(ObjectMapper objectMapper, HttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.httpClient = httpClient;
	}
	public void markArticlesAsRead(Set<String> readArticleIds) throws IOException, InterruptedException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("articleIds", readArticleIds);

        HttpRequest request = HttpRequestBuilder.buildRequest("POST", ApiRoutes.ARTICLE_HISTORY_ROUTE, HttpRequestBuilder.getAuthHeader(), objectMapper.writeValueAsString(requestBody));
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		System.out.println(apiResponse.getMessage());
    }
}
