package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.ExternalServer;
import com.newsfeed.service.ExternalServerService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/servers/*")
public class AdminServerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String PARAM_ACTIVE = "active";
	private static final String PARAM_KEY = "apiKey";
	private static final String IS_UPDATE_LAST_ACCESSED = "updateAccessed";

	private ExternalServerService serverService;
	private ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		this.serverService = ApplicationContext.getObject(ExternalServerService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			String pathInfo = request.getPathInfo();
			if (pathInfo != null && pathInfo.length() > 1) {
				String serverIdString = pathInfo.substring(1);
				int serverId = Integer.parseInt(serverIdString);
				handleGetServerById(serverId, response);
			} else {
				handleGetAllServers(response);
			}
		} catch (NumberFormatException exception) {
			handleBadRequestError(response, Messages.INVALID_SERVER_ID);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setStatus(HttpServletResponse.SC_OK);
	    response.setContentType("application/json");

	    try {
	        String pathInfo = request.getPathInfo();
	        if (pathInfo == null || pathInfo.length() <= 1) {
	            handleBadRequestError(response, Messages.INVALID_SERVER_ID);
	            return;
	        }

	        int serverId = Integer.parseInt(pathInfo.substring(1));

	        String activeParam = request.getParameter(PARAM_ACTIVE);
	        String apiKey = request.getParameter(PARAM_KEY);
	        String updateAccessed = request.getParameter(IS_UPDATE_LAST_ACCESSED);

	        if (activeParam != null) {
	            handleUpdateStatus(response, serverId, activeParam);
	        } else if (apiKey != null) {
	            handleUpdateApiKey(response, serverId, apiKey);
	        } else if ("true".equalsIgnoreCase(updateAccessed)) {
	            handleUpdateLastAccessed(response, serverId);
	        } else {
	            handleBadRequestError(response, Messages.INVALID_SERVER_DETAILS);
	        }

	    } catch (NumberFormatException e) {
	        handleBadRequestError(response, Messages.INVALID_SERVER_ID);
	    }
	}

	private void handleGetServerById(int serverId, HttpServletResponse response) throws IOException {
		Optional<ExternalServer> server = serverService.findById(serverId);
		if (server.isPresent()) {
			objectMapper.writeValue(response.getWriter(), ApiResponse.success(Messages.GOT_SERVER_DETAILS_SUCESSFULLY, server.get()));
		} else {
			handleNotFoundError(response, Messages.INVALID_SERVER_DETAILS);
		}
	}

	private void handleGetAllServers(HttpServletResponse response) throws IOException {
		List<ExternalServer> servers = serverService.findAll();
		objectMapper.writeValue(response.getWriter(),
				ApiResponse.success(Messages.GOT_SERVER_DETAILS_SUCESSFULLY, servers));
	}

	private void handleUpdateStatus(HttpServletResponse response, int serverId, String activeParam)
			throws IOException {
		boolean isActive = Boolean.parseBoolean(activeParam);
		serverService.updateActiveStatus(serverId, isActive);
		handleUpdateSucessResponse(response, Messages.SERVER_DETAILS_UPDATED_SUCESSFULLY);
	}

	private void handleUpdateLastAccessed(HttpServletResponse response, int serverId) throws IOException {
		serverService.updateLastAccessed(serverId);
		handleUpdateSucessResponse(response, Messages.SERVER_DETAILS_UPDATED_SUCESSFULLY);
	}

	private void handleUpdateApiKey(HttpServletResponse response, int serverId, String apiKey)
			throws IOException {
		if (apiKey == null || apiKey.isBlank()) {
			handleBadRequestError(response, Messages.INVALID_API_KEY);
			return;
		}

		serverService.updateActiveKey(serverId, apiKey);
		handleUpdateSucessResponse(response, Messages.SERVER_DETAILS_UPDATED_SUCESSFULLY);
	}

	private void handleUpdateSucessResponse(HttpServletResponse response, String message) throws IOException {
		response.setContentType("application/json");
		objectMapper.writeValue(response.getWriter(), ApiResponse.success(message));
	}

	private void handleBadRequestError(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		objectMapper.writeValue(response.getWriter(), ApiResponse.error(message));
	}

	private void handleNotFoundError(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.setContentType("application/json");
		objectMapper.writeValue(response.getWriter(), ApiResponse.error(message));
	}
}
