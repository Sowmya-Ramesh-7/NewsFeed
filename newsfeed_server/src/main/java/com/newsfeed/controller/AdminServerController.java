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
	private static final String IS_UPDATE_LAST_ACCESSED = "updateAccessed";

	private static final long serialVersionUID = 1L;

	private static final String PARAM_NAME = "name";
	private static final String PARAM_ACTIVE = "active";
	private static final String PARAM_KEY = "apiKey";

	private transient ExternalServerService serverService;
	private transient ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		this.serverService = ApplicationContext.getObject(ExternalServerService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		String apiName = request.getParameter(PARAM_NAME);
		if (apiName != null) {
			handleGetServerByName(apiName, response);
		} else {
			handleGetAllServers(response);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String apiName = request.getParameter(PARAM_NAME);
		String activeParam = request.getParameter(PARAM_ACTIVE);
		String apiKey = request.getParameter(PARAM_KEY);
		String updateAccessed = request.getParameter(IS_UPDATE_LAST_ACCESSED);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		
		if (apiName == null) {
			handleBadRequestError(response, Messages.INVALID_API_NAME);
		} else if (activeParam != null) {
			handleUpdateStatus(request, response, apiName);
		} else if (apiKey != null) {
			handleUpdateApiKey(request, response, apiName);
		} else if ("true".equalsIgnoreCase(updateAccessed)) {
			handleUpdateLastAccessed(response, apiName);
		}
		handleBadRequestError(response, Messages.INVALID_SERVER_DETAILS);
	}

	private void handleGetServerByName(String apiName, HttpServletResponse response) throws IOException {
	    Optional<ExternalServer> server = serverService.findByApiName(apiName);
	    if (server.isPresent()) {
	        objectMapper.writeValue(response.getWriter(), ApiResponse.success(Messages.GOT_SERVER_DETAILS_SUCESSFULLY, server.get()));
	    } else {
	    	handleNotFoundError(response, Messages.INVALID_SERVER_DETAILS);
	    }
	}

	private void handleGetAllServers(HttpServletResponse response) throws IOException {
	    List<ExternalServer> servers = serverService.findAll();
	    objectMapper.writeValue(response.getWriter(), ApiResponse.success(Messages.GOT_SERVER_DETAILS_SUCESSFULLY, servers));
	}

	private void handleUpdateStatus(HttpServletRequest request, HttpServletResponse response, String apiName)
			throws IOException {
		boolean isActive = Boolean.parseBoolean(request.getParameter(PARAM_ACTIVE));
		serverService.updateActiveStatus(apiName, isActive);
		handleUpdateSucessResponse(response, Messages.SERVER_DETAILS_UPDATED_SUCESSFULLY);
	}

	private void handleUpdateLastAccessed(HttpServletResponse response, String apiName) throws IOException {
		serverService.updateLastAccessed(apiName);
		handleUpdateSucessResponse(response, Messages.SERVER_DETAILS_UPDATED_SUCESSFULLY);
	}

	private void handleUpdateApiKey(HttpServletRequest request, HttpServletResponse response, String apiName)
			throws IOException {
		String apiKey = request.getParameter(PARAM_KEY);
		if (apiKey == null || apiKey.isBlank()) {
			handleBadRequestError(response, Messages.INVALID_API_KEY );
			return;
		}

		serverService.updateActiveKey(apiName, apiKey);
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
