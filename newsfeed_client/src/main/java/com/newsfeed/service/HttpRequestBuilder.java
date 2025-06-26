package com.newsfeed.service;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Map;

import com.newsfeed.util.constants.Messages;

public class HttpRequestBuilder {

	public static HttpRequest buildRequest(String method, String url, Map<String, String> headers, String jsonBody) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                                                 .uri(URI.create(url));
        
        headers.forEach((key, value)->builder.header(key, value));
        
        switch (method.toUpperCase()) {
            case "GET":
                builder.GET();
                break;
            case "POST":
                builder.POST(BodyPublishers.ofString(jsonBody));
                break;
            case "PUT":
                builder.PUT(BodyPublishers.ofString(jsonBody));
                break;
            case "DELETE":
                builder.DELETE();
                break;
            default:
                throw new IllegalArgumentException(String.format(Messages.UNSUPPORTED_HTTP_METHOD,"method"));
        }

        return builder.build();
    }
}