package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.User;
import com.newsfeed.util.ApplicationContext;

public class UserAuthenticationService {
	
	public UserAuthenticationService(){};

	private static final String BASE_URL = "http://localhost:8080/newsfeed_server";
	private static final String SIGNUP_ROUTE = "/auth/signup";
	private static final ObjectMapper objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	private static final HttpClient httpClient  = ApplicationContext.getObject(HttpClient.class);

	public boolean login(String userId, String password) {
		return false;
	}

	public void logout() {
		
	}

	public void signup(User user) throws IOException, InterruptedException {
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		HttpRequest signupRequest = HttpRequestBuilder.buildRequest("POST", BASE_URL + SIGNUP_ROUTE, headers, objectMapper.writeValueAsString(user));
		HttpResponse<String> response = httpClient.send(signupRequest, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
	}
	
	

}
