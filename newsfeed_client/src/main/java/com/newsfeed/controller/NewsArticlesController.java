package com.newsfeed.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.NewsArticleService;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;

public class NewsArticlesController {
	private NewsArticleService newsArticleService;
	private NewsCategoryController newsCategoryController;
	
	public NewsArticlesController(NewsArticleService newsArticleService, NewsCategoryController newsCategoryController){
		this.newsArticleService = newsArticleService;
		this.newsCategoryController = newsCategoryController;
	}

	public List<NewsArticle> getArticlesForToday() throws IOException, InterruptedException {
	    return newsArticleService.getArticlesForToday();
	}

	public List<NewsArticle> getArticlesByDateRange() throws IOException, InterruptedException {
		LocalDate startDate = InputUtil.readDate(Messages.ENTER_START_DATE);
        LocalDate endDate = InputUtil.readDate(Messages.ENTER_END_DATE);
        
        if (startDate.isAfter(endDate)) {
            System.out.println(Messages.START_DATE_CANNOT_BE_AFTER_END_DATE);
            return Collections.emptyList();
        }
        
        String categoryId = newsCategoryController.getCategoryInputFromUser();
        
	    return newsArticleService.getArticlesByFilters(startDate, endDate, categoryId);
	}
	
	
}
