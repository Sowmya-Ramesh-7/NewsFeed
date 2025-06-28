package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.model.User;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.JwtUtil;

public class UserAuthenticationService {
	
	private static final String BASE_URL = "http://localhost:8080/newsfeed_server";
	private static final String SIGNUP_ROUTE = "/auth/signup";
	private static final String LOGIN_ROUTE = "/auth/login";
	private static final ObjectMapper objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	private static final HttpClient httpClient  = ApplicationContext.getObject(HttpClient.class);
	
	public void signup(User user) throws IOException, InterruptedException {
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		HttpRequest signupRequest = HttpRequestBuilder.buildRequest("POST", BASE_URL + SIGNUP_ROUTE, headers, objectMapper.writeValueAsString(user));
		HttpResponse<String> response = httpClient.send(signupRequest, HttpResponse.BodyHandlers.ofString());
		ApiResponse signupResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		if(signupResponse.isSuccess()){
			System.out.println(signupResponse.getMessage());
		}else {
			System.out.println(signupResponse.getMessage());
		}
	}
	
	public boolean login(String email, String password) throws IOException, InterruptedException {
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		User user = new User();
		user.setEmailAddress(email);
		user.setPassword(password);
		
		HttpRequest loginRequest = HttpRequestBuilder.buildRequest("POST", BASE_URL + LOGIN_ROUTE, headers, objectMapper.writeValueAsString(user));
		HttpResponse<String> response = httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());
		ApiResponse loginResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		
		if (loginResponse.isSuccess()) {
			HashMap<String,String> data = (HashMap<String,String>)loginResponse.getData();
            String token = data.get("token").toString();
            JwtUtil.saveToken(token);
            System.out.println(loginResponse.getMessage());
            return true;
        }
		System.out.println(loginResponse.getMessage());
		return false;
	}

	public void logout() {
		
	}
}
