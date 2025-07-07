package com.newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.service.ArticleHistoryService;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.constants.Messages;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/history/articles"})
public class ArticleHistoryController extends HttpServlet {

    private ArticleHistoryService articleHistoryService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.articleHistoryService = ApplicationContext.getObject(ArticleHistoryService.class);
        this.objectMapper = ApplicationContext.getObject(ObjectMapper.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try (BufferedReader reader = request.getReader()) {
            Map<String, Object> requestBody = objectMapper.readValue(reader, Map.class);
            String userId = (String) request.getAttribute("userId");
            List<String> articleIds = (List<String>) requestBody.get("articleIds");

            if (userId == null || articleIds == null || articleIds.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(response.getWriter(), ApiResponse.error("userId and articleIds are required"));
                return;
            }

            articleHistoryService.markArticlesRead(userId, articleIds);

            objectMapper.writeValue(response.getWriter(), ApiResponse.success(Messages.ARTICLE_MARKED_AS_READ));

        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(), ApiResponse.error(exception.getMessage()));
        }
    }

}
