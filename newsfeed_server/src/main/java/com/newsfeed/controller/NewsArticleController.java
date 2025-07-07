package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.ArticleReactionService;
import com.newsfeed.service.NewsArticleService;
import com.newsfeed.service.NotificationService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/articles/*")
public class NewsArticleController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private NewsArticleService newsArticleService;
	private ObjectMapper objectMapper;
	private ArticleReactionService articleReactionService;
	private NotificationService notificationService;

	@Override
	public void init() throws ServletException {
		this.newsArticleService = ApplicationContext.getObject(NewsArticleService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
		this.articleReactionService = ApplicationContext.getObject(ArticleReactionService.class);
		this.notificationService = ApplicationContext.getObject(NotificationService.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);

		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String category = request.getParameter("category");
		String searchQuery = request.getParameter("q");
		ApiResponse apiResponse;
		try {
			List<NewsArticle> articles = new ArrayList<NewsArticle>();
			if (searchQuery != null) {
				articles = newsArticleService.getArticlesByText(searchQuery);
			} else {
				articles = newsArticleService.getArticles(start, end, category);
			}
			
			if (articles.size() == 0) {
				apiResponse = ApiResponse.success(Messages.NO_ARTICLES_FOUND, articles);
			} else {
				apiResponse = ApiResponse.success(Messages.FOUND_ARTICLES, articles);
			}
			objectMapper.writeValue(response.getWriter(), apiResponse);

		} catch (Exception exception) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			ApiResponse error = ApiResponse.error("Invalid request: " + exception.getMessage());
			objectMapper.writeValue(response.getWriter(), error);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		try {
			String path = request.getRequestURI();
			String[] parts = path.split("/");
			String articleId = parts[3];

			String userId = (String) request.getAttribute("userId");
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.UNAUTHORIZED));
				return;
			}

			BufferedReader reader = request.getReader();
			Map<String, String> requestBody = objectMapper.readValue(reader, Map.class);

			String reaction = requestBody.get("reaction");
			if (reaction == null || (!reaction.equalsIgnoreCase("LIKE") && !reaction.equalsIgnoreCase("DISLIKE")
					&& !reaction.equalsIgnoreCase("REPORT"))) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.INVALID_REACTION_TYPE));
				return;
			}

			boolean updated = articleReactionService.reactToArticle(userId, articleId, reaction.toUpperCase());
			String message = updated ? "You " + reaction.toLowerCase() + "d this article."
					: "You have already " + reaction.toLowerCase() + "d this article.";

			if (reaction.equalsIgnoreCase("REPORT")) {
				notificationService.sendReportAlertToAdmins(articleId, userId);
			}
			objectMapper.writeValue(response.getWriter(), ApiResponse.success(message));

		} catch (Exception exception) {
			exception.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			objectMapper.writeValue(response.getWriter(), ApiResponse.error(exception.getMessage()));
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("application/json");

	    try {
	        Map<String, String> requestBody = objectMapper.readValue(request.getReader(), Map.class);
	        String articleId = requestBody.get("articleId");
	        String categoryId = requestBody.get("categoryId");
	        String keyword = requestBody.get("keyword");
	        boolean updated = false;
	        if (articleId != null) {
	        	updated = newsArticleService.hideArticleById(articleId);
	        }else if (categoryId != null) {
	            updated = newsArticleService.hideArticlesByCategory(categoryId);
	        }else if (keyword != null) {
	            updated = newsArticleService.hideArticlesByKeyword(keyword);
	        }else {
	        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		        objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.HIDE_CRITERIA_REQUIRED));
		        return;
	        }
	        String message = updated ? Messages.ARTICLE_HIDDEN_SUCCESS : Messages.ARTICLE_NOT_FOUND_OR_ALREADY_HIDDEN;
            objectMapper.writeValue(response.getWriter(), ApiResponse.success(message));
            return;

	    } catch (Exception exception) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        objectMapper.writeValue(response.getWriter(), ApiResponse.error(exception.getMessage()));
	    }
	}


}
