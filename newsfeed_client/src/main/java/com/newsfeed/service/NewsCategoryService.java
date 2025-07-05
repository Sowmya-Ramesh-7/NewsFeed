package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.model.NewsCategory;
import com.newsfeed.util.constants.ApiRoutes;

public class NewsCategoryService {

	private HttpClient httpClient;
	private ObjectMapper objectMapper;

	public NewsCategoryService(ObjectMapper objectMapper, HttpClient httpClient) {
		this.httpClient = httpClient;
		this.objectMapper = objectMapper;
	}

	public Map<String, NewsCategory> getAllCategories() throws IOException, InterruptedException {
		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
		HttpRequest request = HttpRequestBuilder.buildRequest("GET", ApiRoutes.CATEGORY_ROUTE, headers);
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

		if (response.statusCode() == 200) {
			Object data = apiResponse.getData();
			return objectMapper.convertValue(data,
					objectMapper.getTypeFactory().constructMapType(Map.class, String.class, NewsCategory.class));
		} else {
			System.out.println(apiResponse.getMessage());
		}
		return Collections.emptyMap();
	}

}
