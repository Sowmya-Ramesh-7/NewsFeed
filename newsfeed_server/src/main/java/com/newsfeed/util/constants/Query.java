package com.newsfeed.util.constants;

public class Query {
	public static final String INSERT_USER = "INSERT INTO users (user_id, name, phone_number, email_address, is_admin, password) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email_address = ?";
	public static final String INSERT_INTO_NEWS_ARTICLES = "INSERT IGNORE INTO news_articles (article_id, title, content, source_link, article_url, category_id, image_url, published_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_EXTERNAL_SERVER_BY_ID = "SELECT * FROM external_server WHERE server_id = ?";
	public static final String GET_ALL_EXTERNAL_SERVERS = "SELECT * FROM external_server";
	public static final String GET_ALL_NEWS_CATEGORIES_WITH_KEYWORD = "SELECT category.category_id, category.category, keyword.keyword_id, keyword.keyword FROM news_categories category LEFT JOIN category_keywords keyword ON category.category_id = keyword.category_id";
}
