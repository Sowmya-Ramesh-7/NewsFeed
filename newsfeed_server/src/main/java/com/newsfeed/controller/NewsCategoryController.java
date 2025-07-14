package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.NewsCategory;
import com.newsfeed.service.NewsCategoryService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/categories")
public class NewsCategoryController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private transient NewsCategoryService categoryService;
	private transient ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		this.categoryService = ApplicationContext.getObject(NewsCategoryService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Map<String, NewsCategory> categories = categoryService.getAllCategories();
			ApiResponse apiResponse = ApiResponse.success(Messages.GOT_ALL_CATEGORIES_SUCCESSFULLY, categories);
			response.setContentType("application/json");
			objectMapper.writeValue(response.getWriter(), apiResponse);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ApiResponse errorResponse = ApiResponse.error(Messages.FAILED_TO_GET_CATEGORIES);
			objectMapper.writeValue(response.getWriter(), errorResponse);
		}
	}
	
}
