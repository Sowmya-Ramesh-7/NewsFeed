package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.model.NewsCategory;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class NewsCategoryDao {
	private CategoryKeywordDao keywordDao;

	public NewsCategoryDao(CategoryKeywordDao keywordDao) {
		this.keywordDao = keywordDao;
	}

	public Map<String, NewsCategory> getAllCategories() {
		Map<String, NewsCategory> categories = new HashMap<String, NewsCategory>();

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(Query.GET_ALL_NEWS_CATEGORIES_WITH_KEYWORD);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				String categoryId = resultSet.getString("category_id");
				NewsCategory category = categories.getOrDefault(categoryId, new NewsCategory());

				category.setCategoryId(categoryId);
				category.setCategory(resultSet.getString("category"));
				Map<String, String> keywordMap = category.getKeywordsMap();
				if (keywordMap == null) {
					keywordMap = new HashMap<String, String>();
				}

				String keyword = resultSet.getString("keyword");
				String keywordId = resultSet.getString("keyword_id");

				if (keyword != null && keywordId != null) {
					keywordMap.put(keywordId, keyword);
				}
				category.setKeywordsMap(keywordMap);
				categories.put(categoryId, category);

			}

		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}

		return categories;
	}

	public boolean addCategory(NewsCategory category) {
		try (Connection connection = DBConnect.getConnection()) {
			if (isCategoryExists(connection, category.getCategory())) {
				return false;
			}

			insertCategory(connection, category.getCategoryId(), category.getCategory());
			keywordDao.insertKeywords(category.getKeywordsMap(), category.getCategoryId());

			return true;
		} catch (SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
			throw new ServerException(Messages.FAILED_TO_ADD_CATEGORY);
		}
	}

	private boolean isCategoryExists(Connection connection, String name) throws SQLException {
		String query = Query.SELECT_COUNT_FROM_CATEGORY_BY_NAME;
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name.trim());
			ResultSet rs = preparedStatement.executeQuery();
			return rs.next() && rs.getInt(1) > 0;
		}
	}

	private void insertCategory(Connection connection, String id, String name) throws SQLException {
		String query = Query.INSERT_INTO_CATEGORY;
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, name);
			preparedStatement.executeUpdate();
		}
	}
}
