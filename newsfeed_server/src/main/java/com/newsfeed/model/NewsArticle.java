package com.newsfeed.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.newsfeed.service.NewsSourceDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
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

	@JsonAlias("snippet")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	@JsonDeserialize(using = NewsSourceDeserializer.class)
	public void setSource(String source) {
		this.source = source;
	}

	public String getArticleUrl() {
		return articleUrl;
	}

	@JsonProperty("url")
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

	@JsonProperty("urlToImage")
	@JsonAlias("image_url")
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LocalDateTime getPublishedDate() {
		return publishedDate;
	}

	@JsonProperty("publishedAt")
	@JsonAlias("published_at")
	public void setPublishedDate(LocalDateTime publishedDate) {
		this.publishedDate = publishedDate;
	}
}
