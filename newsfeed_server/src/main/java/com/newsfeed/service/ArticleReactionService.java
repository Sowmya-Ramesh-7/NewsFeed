package com.newsfeed.service;

import com.newsfeed.dao.ArticleReactionDao;
import com.newsfeed.dao.NewsArticleDao;

public class ArticleReactionService {

    private static final int REPORT_THRESHOLD = 5;
	private final ArticleReactionDao reactionDao;
	private NewsArticleDao newsArticleDao;

    public ArticleReactionService(ArticleReactionDao reactionDao, NewsArticleDao newsArticleDao) {
        this.reactionDao = reactionDao;
        this.newsArticleDao = newsArticleDao;
    }

    public boolean reactToArticle(String userId, String articleId, String reactionType) {
        boolean updated = reactionDao.saveOrUpdateReaction(userId, articleId, reactionType);

        if (reactionType.equalsIgnoreCase("REPORT") && updated) {
            int reportCount = reactionDao.getReportCountForArticle(articleId);
            if (reportCount > REPORT_THRESHOLD) {
                newsArticleDao.hideArticleById(articleId);
            }
        }

        return updated;
    }

}