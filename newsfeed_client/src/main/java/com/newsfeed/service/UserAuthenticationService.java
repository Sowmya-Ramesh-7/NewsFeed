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
import com.newsfeed.util.JwtUtil;
import com.newsfeed.util.constants.ApiRoutes;

public class UserAuthenticationService {
	private ObjectMapper objectMapper;
	private HttpClient httpClient;
	private boolean isLoggedIn;

	public UserAuthenticationService(ObjectMapper objectMapper, HttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.httpClient = httpClient;
		this.isLoggedIn = false;
	}

	public void signup(User user) throws IOException, InterruptedException {
		String requestBody = objectMapper.writeValueAsString(user);
		HttpRequest signupRequest = HttpRequestBuilder.buildRequest("POST", ApiRoutes.SIGNUP_ROUTE, requestBody);
		HttpResponse<String> response = httpClient.send(signupRequest, HttpResponse.BodyHandlers.ofString());
		ApiResponse signupResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		if (signupResponse.isSuccess()) {
			System.out.println(signupResponse.getMessage());
		} else {
			System.out.println(signupResponse.getMessage());
		}
	}

	public Map<String, String> login(String email, String password) throws IOException, InterruptedException {
		User user = new User();
		user.setEmailAddress(email);
		user.setPassword(password);

		HttpRequest loginRequest = HttpRequestBuilder.buildRequest("POST", ApiRoutes.LOGIN_ROUTE, objectMapper.writeValueAsString(user));
		HttpResponse<String> response = httpClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());
		ApiResponse loginResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		Map<String, String> data = new HashMap<String, String>();
		if (loginResponse.isSuccess()) {
			data = (HashMap<String, String>) loginResponse.getData();
			String token = data.get("token").toString();
			JwtUtil.saveToken(token);
			isLoggedIn = true;
			System.out.println(loginResponse.getMessage());
			return data;
		}
		
		System.out.println(loginResponse.getMessage());
		return data;
	}

	public boolean logout() throws IOException, InterruptedException {
		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();

		HttpRequest request = HttpRequestBuilder.buildRequest("POST", ApiRoutes.LOGOUT_ROUTE, headers);
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		ApiResponse logoutResponse = objectMapper.readValue(response.body(), ApiResponse.class);
		JwtUtil.clearToken();

		System.out.println(logoutResponse.getMessage());
		isLoggedIn = false;
		return isLoggedIn;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}
}
