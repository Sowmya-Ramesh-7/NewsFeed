package com.newsfeed.util.constants;

public class Query {
	public static final String FETCH_NEWS_HISTORY_BASED_ON_PRIORITY = """
			SELECT article.*,
			       IF(reaction.reaction_type IN ('LIKE', 'SAVE'), 1, 0) AS reaction_priority,
			       IF(saved.article_id IS NOT NULL, 1, 0) AS saved_priority,
			       IF(category.category_id IN (
			           SELECT preference.category_id
			           FROM user_preferences preference
			           WHERE preference.user_id = ? AND preference.is_enabled = TRUE
			       ), 1, 0) AS category_priority
			FROM article_read_history history
			JOIN news_articles article ON history.article_id = article.article_id
			LEFT JOIN user_article_reaction reaction
			       ON reaction.article_id = article.article_id AND reaction.user_id = ?
			LEFT JOIN saved_articles saved
			       ON saved.article_id = article.article_id AND saved.user_id = ?
			LEFT JOIN news_categories category
			       ON article.category_id = category.category_id
			WHERE history.user_id = ?
			ORDER BY (reaction_priority + saved_priority + category_priority) DESC, history.read_at DESC
			""";
	
	public static final String INSERT_USER = "INSERT INTO users (user_id, name, phone_number, email_address, is_admin, password) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email_address = ?";
	public static final String INSERT_INTO_NEWS_ARTICLES = "INSERT IGNORE INTO news_articles (article_id, title, content, source_link, article_url, category_id, image_url, published_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_EXTERNAL_SERVER_BY_ID = "SELECT * FROM external_server WHERE server_id = ?";
	public static final String GET_ALL_EXTERNAL_SERVERS = "SELECT * FROM external_server";
	public static final String GET_ALL_NEWS_CATEGORIES_WITH_KEYWORD = "SELECT category.category_id, category.category, keyword.keyword_id, keyword.keyword FROM news_categories category LEFT JOIN category_keywords keyword ON category.category_id = keyword.category_id";
	public static final String INSERT_INTO_CATEGORY = "INSERT INTO news_categories (category_id, category) VALUES (?, ?)";
	public static final String SELECT_COUNT_FROM_CATEGORY_BY_NAME = "SELECT COUNT(*) FROM news_categories WHERE category = ?";
	public static final String INSERT_INTO_CATEGORY_KEYWORDS = "INSERT INTO category_keywords (keyword_id, keyword, category_id) VALUES (?, ?, ?)";
	public static final String SELECT_CATEGORIES_BY_ID = "SELECT keyword_id, keyword FROM category_keywords WHERE category_id = ?";
	public static final String INSERT_INTO_SAVED_ARTICLES = "INSERT INTO saved_articles (id, user_id, article_id) VALUES (?, ?, ?)";
	public static final String GET_ALL_SAVED_ARTICLES = "SELECT article.* FROM saved_articles saved JOIN news_articles article ON saved.article_id = article.article_id WHERE saved.user_id = ? AND is_hidden = false";
	public static final String SEARCH_BY_ARTILCES_KEYWORD = "SELECT * FROM news_articles WHERE is_hidden = false AND MATCH(title, content) AGAINST (? IN NATURAL LANGUAGE MODE) ORDER BY published_date DESC";
	public static final String GET_USER_REACTION = "SELECT reaction_type FROM user_article_reaction WHERE user_id = ? AND article_id = ?";
	public static final String UPSERT_USER_REACTION = "INSERT INTO user_article_reaction (user_id, article_id, reaction_type) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE reaction_type = VALUES(reaction_type)";
	public static final String INSERT_NOTIFICATION = "INSERT INTO notification_history (notification_id, user_id, message, article_id, title, sent_at, is_read) VALUES (?, ?, ?, ?, ?, ?, ?)";
	public static final String SELECT_ADMINS = "SELECT * FROM users WHERE is_admin = true";
	public static final String GET_UNREAD_NOTIFICATION_FOR_USER = "SELECT * FROM notification_history WHERE user_id = ? AND is_read = false ORDER BY sent_at DESC";
	public static final String HIDE_ARTICLE_BY_ID = "UPDATE news_articles SET is_hidden = TRUE WHERE article_id = ?";
	public static final String HIDE_ARTICLE_BY_CATEGORY_ID = "UPDATE news_articles SET is_hidden = TRUE WHERE category_id = ?";;
	public static final String HIDE_ARTICLE_BY_KEYWORD = "UPDATE news_articles SET is_hidden = TRUE WHERE MATCH(title, content) AGAINST(?)";
	public static final String SELECT_COUNT_OF_REPORT_ARTICLE = "SELECT COUNT(*) FROM user_article_reaction WHERE article_id = ? AND reaction_type = 'REPORT'";
	public static final String INSERT_NOTIFICATION_PREFERENCES = "INSERT INTO user_preferences (user_id, category_id, is_enabled) VALUES (?, ?, ?)";
	public static final String UPSERT_CATEGORY_NOTIFICATION_PREFERENCES = "INSERT INTO user_preferences (user_id, category_id, is_enabled) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE is_enabled = VALUES(is_enabled)";
	public static final String GET_CATEGORY_PREFERENCES_BY_USER_ID = "SELECT category_id, is_enabled FROM user_preferences WHERE user_id = ?";
	public static final String INSERT_INTO_ARTICLE_READ_HISTORY = "INSERT IGNORE INTO article_read_history (user_id, article_id, read_at) VALUES (?, ?, ?)";

}
