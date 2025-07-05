package com.newsfeed.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.User;
import com.newsfeed.service.UserAuthenticationService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

@WebServlet("/auth/*")
public class UserAuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ObjectMapper objectMapper;
	private UserAuthenticationService userAuthenticationService;

	@Override
	public void init() throws ServletException {
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
		this.userAuthenticationService = ApplicationContext.getObject(UserAuthenticationService.class);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (path == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.ENDPOINT_NOT_FOUND));
			return;
		}

		switch (path) {
		case "/signup":
			handleSignup(request, response);
			break;
		case "/login":
			handleLogin(request, response);
			break;
		case "/logout":
			handleLogout(request, response);
			break;
		default:
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.ENDPOINT_NOT_FOUND));
		}
	}

	private void handleSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = objectMapper.readValue(request.getReader(), User.class);
		String userId = userAuthenticationService.signup(user);

		int statusCode;
		ApiResponse signupResponse;

		if (!userId.isBlank()) {
			statusCode = HttpServletResponse.SC_CREATED;
			signupResponse = ApiResponse.success(String.format(Messages.SIGNUP_SUCCESS, userId));
		} else {
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			signupResponse = ApiResponse.error(Messages.USER_ALREADY_EXISTS);
		}

		response.setStatus(statusCode);
		objectMapper.writeValue(response.getWriter(), signupResponse);
	}

	private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = objectMapper.readValue(request.getReader(), User.class);
		Map<String, String> loginResponse = userAuthenticationService.login(user.getEmailAddress(), user.getPassword());

		int statusCode;
		ApiResponse apiResponse;

		if (loginResponse.containsKey("token") && !loginResponse.get("token").isBlank()) {
			statusCode = HttpServletResponse.SC_OK;
			apiResponse = ApiResponse.success(Messages.LOGIN_SUCCESS, loginResponse);
		} else {
			statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			apiResponse = ApiResponse.error(Messages.INVALID_CREDENTIALS);
		}

		response.setStatus(statusCode);
		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setStatus(HttpServletResponse.SC_OK);
	    ApiResponse logoutResponse = ApiResponse.success(Messages.LOGOUT_SUCCESS);
	    objectMapper.writeValue(response.getWriter(), logoutResponse);
	}
}
