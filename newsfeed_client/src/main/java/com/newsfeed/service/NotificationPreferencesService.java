package com.newsfeed.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.util.constants.ApiRoutes;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NotificationPreferencesService {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public NotificationPreferencesService(ObjectMapper objectMapper, HttpClient httpClient) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    public Map<String, Boolean> getCategoryPreferences() throws IOException, InterruptedException {
        Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
        HttpRequest request = HttpRequestBuilder.buildRequest("GET", ApiRoutes.NOTIFICATION_CONFIG_ROUTE, headers);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

        if (response.statusCode() == 200 && apiResponse.isSuccess()) {
            Object data = apiResponse.getData();
            return objectMapper.convertValue(data,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Boolean.class));
        }

        System.out.println(apiResponse.getMessage());
        return Collections.emptyMap();
    }

    public boolean upsertCategoryPreference(String categoryId, boolean isEnabled) throws IOException, InterruptedException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("categoryId", categoryId);
        requestBody.put("isEnabled", isEnabled);

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
        HttpRequest request = HttpRequestBuilder.buildRequest("PUT", ApiRoutes.NOTIFICATION_CONFIG_ROUTE + "/category", headers, body);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

        System.out.println(apiResponse.getMessage());
        return apiResponse.isSuccess();
    }
}
