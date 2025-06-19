package com.newsfeed.util.constants;

public class Query {
	public static final String INSERT_USER = "INSERT INTO users (user_id, name, phone_number, email_address, is_admin, password) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email_address = ?";
	public static final String INSERT_INTO_NEWS_ARTICLES = "INSERT IGNORE INTO news_articles (article_id, title, content, source_link, article_url, category_id, image_url, published_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_EXTERNAL_SERVER_BY_NAME = "SELECT * FROM external_server WHERE api_name = ?";
	public static final String GET_ALL_EXTERNAL_SERVERS = "SELECT * FROM external_server";
}
