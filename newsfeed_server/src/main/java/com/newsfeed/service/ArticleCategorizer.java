package com.newsfeed.service;

import java.util.*;

import com.newsfeed.dao.NewsCategoryDao;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.model.NewsCategory;

public class ArticleCategorizer {

	private static final String DEFAULT_CATEGORY_ID = "CATEGORY_0";
	private final NewsCategoryDao categoryDao;

	public ArticleCategorizer(NewsCategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public void categorize(List<NewsArticle> articles) {
		Map<String, NewsCategory> categories = categoryDao.getAllCategories();

		for (NewsArticle article : articles) {
			String content = (article.getTitle() + " " + article.getContent()).toLowerCase();

			String bestCategoryId = "";
			int maxScore = 0;

			for (NewsCategory category : categories.values()) {
				int score = 0;

				for (String keyword : category.getKeywordsMap().values()) {
					if (content.contains(keyword.trim().toLowerCase())) {
						score++;
					}
				}

				if (score > maxScore) {
					maxScore = score;
					bestCategoryId = category.getCategoryId();
				}
			}

			article.setCategoryId(!bestCategoryId.isBlank() ? bestCategoryId : DEFAULT_CATEGORY_ID);
		}
	}
}
