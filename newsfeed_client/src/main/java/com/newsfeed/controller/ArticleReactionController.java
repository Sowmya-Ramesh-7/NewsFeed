package com.newsfeed.controller;

import com.newsfeed.service.ArticleReactionService;

import java.io.IOException;

public class ArticleReactionController {

	private ArticleReactionService articleReactionService;

	public ArticleReactionController(ArticleReactionService articleReactionService) {
		this.articleReactionService = articleReactionService;
	}

	public void likeArticle(String articleId) throws IOException {
		articleReactionService.likeArticle(articleId);
	}

	public void dislikeArticle(String articleId) throws IOException {
		articleReactionService.dislikeArticle(articleId);
	}

	public void reportArticle(String articleId) throws IOException {
		articleReactionService.reportArticle(articleId);
	}
}
