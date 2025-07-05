package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.NewsArticleService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/articles/*")
public class NewsArticleController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private NewsArticleService newsArticleService;
	private ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		this.newsArticleService = ApplicationContext.getObject(NewsArticleService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_OK);

	    String start = request.getParameter("start");
	    String end = request.getParameter("end");
	    String category = request.getParameter("category");
	    
	    ApiResponse apiResponse;
	    try {
	        System.out.println(category+ start+ end );
	        List<NewsArticle> articles = newsArticleService.getArticles(start, end, category);
	        if(articles.size()==0) {
	        	apiResponse = ApiResponse.success(Messages.NO_ARTICLES_FOUND, articles);
	        }else {
	        	apiResponse = ApiResponse.success(Messages.FOUND_ARTICLES, articles);
	        }
	        objectMapper.writeValue(response.getWriter(), apiResponse);

	    } catch (Exception exception) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        ApiResponse error = ApiResponse.error("Invalid request: " + exception.getMessage());
	        objectMapper.writeValue(response.getWriter(), error);
	    }
	}

}
