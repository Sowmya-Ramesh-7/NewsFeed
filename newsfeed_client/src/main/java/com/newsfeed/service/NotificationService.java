package com.newsfeed.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.model.NotificationHistory;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.ApiRoutes;

import java.io.IOException;
import java.net.http.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotificationService {

    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public NotificationService() {
        this.httpClient = ApplicationContext.getObject(HttpClient.class);
        this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
    }

    public List<NotificationHistory> getUserNotifications() throws IOException, InterruptedException {
        Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
        HttpRequest request = HttpRequestBuilder.buildRequest("GET", ApiRoutes.NOTIFICATION_ROUTE, headers);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
        System.out.println(apiResponse.getMessage());
        if (apiResponse.isSuccess()) {
            return objectMapper.convertValue(apiResponse.getData(), new TypeReference<List<NotificationHistory>>() {});
        }
        return Collections.emptyList();
    }

    public void markNotificationsAsRead(List<String> notificationIds) throws IOException, InterruptedException {
        Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
        String requestBody = objectMapper.writeValueAsString(Map.of("notificationIds", notificationIds));

        HttpRequest request = HttpRequestBuilder.buildRequest("POST", ApiRoutes.NOTIFICATIONS_READ, headers, requestBody);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
        System.out.println(apiResponse.getMessage());
    }

}
