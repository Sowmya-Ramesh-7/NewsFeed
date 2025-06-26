package com.newsfeed.model;

import java.time.LocalDateTime;

public class NewsArticle {
    private String articleId;
    private String title;
    private String content;
    private String source;
    private String articleUrl;
    private String categoryId;
    private String imageUrl;
	private LocalDateTime publishedDate;
    
	public String getArticleId() {
		return articleId;
	}
	
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
	public String getArticleUrl() {
		return articleUrl;
	}
	
	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LocalDateTime getPublishedDate() {
		return publishedDate;
	}
	
	public void setPublishedDate(LocalDateTime publishedDate) {
		this.publishedDate = publishedDate;
	}
	
}
