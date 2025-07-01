package com.newsfeed.service;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.HashMap;
import java.util.Map;

import com.newsfeed.util.JwtUtil;
import com.newsfeed.util.constants.Messages;

public class HttpRequestBuilder {
	
	public static HttpRequest buildRequest(String method, String url) {
		return buildRequest(method, url, new HashMap<String, String>(), null);
    }
	
	public static HttpRequest buildRequest(String method, String url, String jsonBody) {
		return buildRequest(method, url, new HashMap<String, String>(), jsonBody);
    }
	
	public static HttpRequest buildRequest(String method, String url, Map<String, String> headers) {
		return buildRequest(method, url, headers, null);
    }

	public static HttpRequest buildRequest(String method, String url, Map<String, String> headers, String jsonBody) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                                                 .uri(URI.create(url));
        
        headers.forEach((key, value)->builder.header(key, value));
        BodyPublisher requestBody = (jsonBody != null) ? BodyPublishers.ofString(jsonBody) : BodyPublishers.noBody();
        
        switch (method.toUpperCase()) {
            case "GET":
                builder.GET();
                break;
            case "POST":
                builder.POST(requestBody);
                break;
            case "PUT":
                builder.PUT(requestBody);
                break;
            case "DELETE":
                builder.DELETE();
                break;
            default:
                throw new IllegalArgumentException(String.format(Messages.UNSUPPORTED_HTTP_METHOD,"method"));
        }

        return builder.build();
    }
	
	public static Map<String, String> getAuthHeader() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json");
		JwtUtil.getToken().ifPresent(token -> headers.put("Authorization", "Bearer " + token));
		return headers;
	}
}