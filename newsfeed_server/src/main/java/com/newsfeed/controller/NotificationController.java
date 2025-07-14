package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.NotificationHistory;
import com.newsfeed.service.NotificationService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = { "/users/id/notifications/*" })
public class NotificationController extends HttpServlet {

	private NotificationService notificationService;
	private ObjectMapper objectMapper;

	@Override
	public void init() {
		this.notificationService = ApplicationContext.getObject(NotificationService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");

		try {
			String path = request.getRequestURI();
			String[] parts = path.split("/");
			if (parts.length < 3) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				objectMapper.writeValue(response.getWriter(), ApiResponse.error("User ID is missing in the path"));
				return;
			}

			String userId = (String) request.getAttribute("userId");

			List<NotificationHistory> notifications = notificationService.getNotificationsForUser(userId);
			ApiResponse apiResponse = ApiResponse.success(
					notifications.size() > 0 ? Messages.FETCHED_NOTIFICATIONS_SUCCESSFULLY : Messages.NO_NOTIFICATIONS,
					notifications);
			objectMapper.writeValue(response.getWriter(), apiResponse);

		} catch (Exception exception) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(exception.getMessage()));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!request.getRequestURI().endsWith("notifications/read")) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.ENDPOINT_NOT_FOUND));
			return;
		}

		response.setContentType("application/json");

		try (BufferedReader reader = request.getReader()) {
			Map<String, Object> requestBody = objectMapper.readValue(reader, Map.class);

			List<String> notificationIds = (List<String>) requestBody.get("notificationIds");

			if (notificationIds == null || notificationIds.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				objectMapper.writeValue(response.getWriter(), ApiResponse.error("notificationIds are required"));
				return;
			}

			notificationService.markNotificationsAsRead(notificationIds);
			objectMapper.writeValue(response.getWriter(), ApiResponse.success("Notifications marked as read"));

		} catch (Exception exception) {
			exception.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(exception.getMessage()));
		}
	}
}
