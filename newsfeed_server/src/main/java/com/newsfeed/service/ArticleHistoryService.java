package com.newsfeed.service;

import java.util.List;

import com.newsfeed.dao.ArticleHistoryDao;

public class ArticleHistoryService {

    private ArticleHistoryDao articleHistoryDao;

    public ArticleHistoryService(ArticleHistoryDao articleHistoryDao) {
        this.articleHistoryDao = articleHistoryDao;
    }
    
    public void markArticlesRead(String userId, List<String> articleIds) {
    	articleHistoryDao.addReadHistory(userId, articleIds);
    }
}
