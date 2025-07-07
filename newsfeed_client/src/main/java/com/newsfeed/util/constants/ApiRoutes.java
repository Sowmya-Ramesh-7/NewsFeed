package com.newsfeed.util.constants;

public class ApiRoutes {
	public static final String BASE_URL = "http://localhost:3000/newsfeed_server";
	public static final String SIGNUP_ROUTE = BASE_URL + "/auth/signup";
	public static final String LOGIN_ROUTE = BASE_URL + "/auth/login";
	public static final String LOGOUT_ROUTE = BASE_URL + "/auth/logout";
	public static final String SERVER_ROUTE = BASE_URL + "/admin/servers";
	public static final String ARTICLES_ROUTE = BASE_URL + "/articles";
	public static final String CATEGORY_ROUTE = BASE_URL + "/categories";
	public static final String ADMIN_CATEGORY_ROUTE = BASE_URL + "/admin/categories";
	public static final String SAVED_ARTICLE = BASE_URL + "/users/id/saved-articles";
	public static final String NOTIFICATION_ROUTE = BASE_URL + "/users/id/notifications";
	public static final String NOTIFICATIONS_READ = NOTIFICATION_ROUTE + "/read";
	public static final String NOTIFICATION_CONFIG_ROUTE = BASE_URL + "/notifications/config";
	public static final String ARTICLE_HISTORY_ROUTE = BASE_URL + "/history/articles";
}
