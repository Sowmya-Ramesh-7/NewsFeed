package com.newsfeed.controller;

import java.io.IOException;
import java.util.List;

import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.SavedArticlesService;

public class SavedArticlesController {

    private final SavedArticlesService savedArticlesService;

    public SavedArticlesController(SavedArticlesService savedArticlesService) {
        this.savedArticlesService = savedArticlesService;
    }

    public void save(String articleId) throws IOException, InterruptedException {
        savedArticlesService.saveArticle(articleId);
    }

    public List<NewsArticle> getSavedArticles() throws IOException, InterruptedException {
        return savedArticlesService.getSavedArticles();
    }
}
