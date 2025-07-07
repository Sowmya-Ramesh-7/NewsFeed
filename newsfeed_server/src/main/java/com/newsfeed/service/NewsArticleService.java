package com.newsfeed.service;

import com.newsfeed.dao.NewsArticleDao;
import com.newsfeed.model.NewsArticle;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class NewsArticleService {

	private NewsArticleDao newsArticleDao;

	public NewsArticleService(NewsArticleDao newsArticleDao) {
		this.newsArticleDao = newsArticleDao;
	}
	
	public void addAll(List<NewsArticle> newsArticles) {
		newsArticleDao.addAll(newsArticles);
	}
	
	public List<NewsArticle> getArticlesByText(String keyword) {
		return newsArticleDao.getArticlesByText(keyword);
	}
	
	public boolean hideArticleById(String articleId) {
        return newsArticleDao.hideArticleById(articleId);
    }

    public boolean hideArticlesByCategory(String categoryId) {
        return newsArticleDao.hideArticlesByCategory(categoryId);
    }

    public boolean hideArticlesByKeyword(String keyword) {
        return newsArticleDao.hideArticlesByKeyword(keyword);
    }

	public List<NewsArticle> getArticles(String start, String end, String category) {
		if(start != null && end != null && category != null) {
	        LocalDate startDate = LocalDate.parse(start);
	        LocalDate endDate = LocalDate.parse(end);
	        return newsArticleDao.getArticlesByFilters(startDate, endDate, category);
		}if(start != null && end != null && category == null) {
	        LocalDate startDate = LocalDate.parse(start);
	        LocalDate endDate = LocalDate.parse(end);
	        return newsArticleDao.getArticlesByFilters(startDate, endDate, category);
		}else if(category != null && !category.isBlank()) {
			return newsArticleDao.getArticlesByCategory(category);
		}
		return Collections.emptyList();
		
	}
}