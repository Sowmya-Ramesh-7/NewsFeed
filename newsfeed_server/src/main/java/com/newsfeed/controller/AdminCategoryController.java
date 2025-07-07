package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.model.NewsCategory;
import com.newsfeed.service.NewsCategoryService;
import com.newsfeed.util.ApplicationContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/categories")
public class AdminCategoryController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private transient NewsCategoryService categoryService;
	private transient ObjectMapper objectMapper;

	@Override
	public void init() throws ServletException {
		this.categoryService = ApplicationContext.getObject(NewsCategoryService.class);
		this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        NewsCategory category = objectMapper.readValue(request.getReader(), NewsCategory.class);
        boolean isSuccess = categoryService.addCategory(category);

        ApiResponse apiResponse = ApiResponse.success(isSuccess ? "Category added successfully" : "Category already exists");
        
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

