package com.newsfeed.service;

import java.util.List;

import com.newsfeed.dao.SavedArticlesDao;
import com.newsfeed.model.NewsArticle;

public class SavedArticlesService {

    private final SavedArticlesDao savedArticlesDao;

    public SavedArticlesService(SavedArticlesDao savedArticlesDao) {
        this.savedArticlesDao = savedArticlesDao;
    }

    public boolean saveArticle(String userId, String articleId) {
        return savedArticlesDao.saveArticle(userId, articleId);
    }

	 public List<NewsArticle> getSavedArticles(String userId) {
        return savedArticlesDao.getSavedArticles(userId);
    }
}
