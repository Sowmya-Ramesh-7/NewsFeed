package com.newsfeed.service;

import com.newsfeed.dao.NewsArticleDao;
import com.newsfeed.model.NewsArticle;

import java.util.List;

public class NewsArticleService {

	private NewsArticleDao newsArticleDao;

	public NewsArticleService(NewsArticleDao newsArticleDao) {
		this.newsArticleDao = newsArticleDao;
	}
	
	public void addAll(List<NewsArticle> newsArticles) {
		newsArticleDao.addAll(newsArticles);
	}
}