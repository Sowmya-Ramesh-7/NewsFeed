package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.SavedArticlesService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/users/id/saved-articles")
public class SavedArticlesController extends HttpServlet {

	private final SavedArticlesService savedArticlesService = ApplicationContext.getObject(SavedArticlesService.class);
	private final ObjectMapper objectMapper = ApplicationContext.getObject(ObjectMapper.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = (String) request.getAttribute("userId");

		if (userId == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.UNAUTHORIZED));
			return;
		}

		Map<String, String> requestBody = objectMapper.readValue(request.getReader(), Map.class);
		String articleId = requestBody.get("articleId");

		if (articleId == null || articleId.isBlank()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.ARTICLE_ID_REQUIRED));
			return;
		}
		boolean isSuccess = savedArticlesService.saveArticle(userId, articleId);
		ApiResponse apiResponse = isSuccess ? ApiResponse.success(Messages.ARTICLE_SAVED_SUCCESSFULLY)
				: ApiResponse.error(Messages.ARTICLE_ALREADY_SAVED);

		response.setContentType("application/json");
		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = (String) request.getAttribute("userId");
		List<NewsArticle> articles = savedArticlesService.getSavedArticles(userId);
		ApiResponse apiResponse = ApiResponse
				.success((articles.size() > 0) ? Messages.FOUND_SAVED_ARTICLES : Messages.NO_SAVED_ARTICLES, articles);

		response.setContentType("application/json");
		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}
