package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.util.constants.ApiRoutes;

public class ArticleReactionService {
	private ObjectMapper objectMapper;
	private HttpClient httpClient;

	public ArticleReactionService(ObjectMapper objectMapper, HttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.httpClient = httpClient;
	}
	public boolean likeArticle(String articleId) {
		Map<String, String> reaction = new HashMap<String, String>();
		reaction.put("reaction", "like");
		return sendReaction(articleId, reaction);
	}

	public boolean dislikeArticle(String articleId) {
		Map<String, String> reaction = new HashMap<String, String>();
		reaction.put("reaction", "dislike");
		return sendReaction(articleId, reaction);
	}

	public boolean reportArticle(String articleId) {
		Map<String, String> reaction = new HashMap<String, String>();
		reaction.put("reaction", "report");
		return sendReaction(articleId, reaction);
	}

	private boolean sendReaction(String articleId, Map<String, String> reaction) {
		try {
			String url = ApiRoutes.ARTICLES_ROUTE + "/" + articleId + "/reactions";
			Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
			String responseBody = objectMapper.writeValueAsString(reaction);
			HttpRequest request = HttpRequestBuilder.buildRequest("POST", url, headers, responseBody);

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
			System.out.println(apiResponse.getMessage());

			return apiResponse.isSuccess();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
}
