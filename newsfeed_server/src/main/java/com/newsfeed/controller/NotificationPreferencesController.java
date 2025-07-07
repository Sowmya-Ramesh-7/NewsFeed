package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.UserNotificationPreference;
import com.newsfeed.service.NotificationPreferencesService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = { "/notifications/config", "/notifications/config/category", "/notifications/config/keywords" })
public class NotificationPreferencesController extends HttpServlet {

	private NotificationPreferencesService notificationPreferencesService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {
		this.notificationPreferencesService = ApplicationContext.getObject(NotificationPreferencesService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		String userId = (String) request.getAttribute("userId");

		try {
			Map<String, Boolean> notificationPreferences = notificationPreferencesService.getNotificationPreferences(userId);
			objectMapper.writeValue(response.getWriter(),
					ApiResponse.success(Messages.FETCHED_PREFERENCES, notificationPreferences));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(e.getMessage()));
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		String path = request.getRequestURI();
		String userId = (String) request.getAttribute("userId");

		try (BufferedReader reader = request.getReader()) {
			
			if (path.endsWith("/category")) {
				UserNotificationPreference userNotificationPreference = objectMapper.readValue(reader, UserNotificationPreference.class);
				userNotificationPreference.setUserId(userId);
				
				notificationPreferencesService.setCategoryPreference(userNotificationPreference);
				objectMapper.writeValue(response.getWriter(), ApiResponse.success(Messages.NOTIFICATION_CONFIGURED));
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.ENDPOINT_NOT_FOUND));
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(e.getMessage()));
		}
	}
}
