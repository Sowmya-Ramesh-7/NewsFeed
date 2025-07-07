package com.newsfeed.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.newsfeed.model.NewsArticle;
import com.newsfeed.service.NewsArticleService;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Prompts;

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

	public List<NewsArticle> getArticlesByText() throws IOException, InterruptedException {
		String searchQuery = InputUtil.readLine(Messages.ENTER_THE_SEARCH_QUERY);
		return newsArticleService.getArticlesByText(searchQuery);
	}

    public void showHideArticleMenu() throws IOException, InterruptedException {
    	boolean isContinue = true;
        while (isContinue) {
            System.out.println("\n" + Messages.HIDE_ARTICLES_HEADER);
            System.out.println("1. Hide by Article ID");
            System.out.println("2. Hide by Category ID");
            System.out.println("3. Hide by Keyword");
            System.out.println("4. Back");

            String choice = InputUtil.readLine(Prompts.ENTER_YOUR_CHOICE);
            switch (choice) {
                case "1":
                    hideByArticleId();
                    break;
                case "2":
                    hideByCategoryId();
                    break;
                case "3":
                    hideByKeyword();
                    break;
                case "4": 
                	isContinue = false;
                    break;
                default:
                    System.out.println(Messages.INVALID_OPTION);
            }
        }
    }

    private void hideByArticleId() throws IOException, InterruptedException {
        String articleId = InputUtil.readLine("Enter Article ID to hide: ");
        newsArticleService.hideArticleById(articleId);
    }

    private void hideByCategoryId() throws IOException, InterruptedException {
        String categoryId = InputUtil.readLine("Enter Category ID to hide articles under: ");
        newsArticleService.hideArticlesByCategory(categoryId);
    }

    private void hideByKeyword() throws IOException, InterruptedException {
        String keyword = InputUtil.readLine("Enter Keyword to hide matching articles: ");
        newsArticleService.hideArticlesByKeyword(keyword);
    }
}
