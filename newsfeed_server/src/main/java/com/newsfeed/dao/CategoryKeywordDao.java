package com.newsfeed.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.newsfeed.exception.ServerException;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

public class CategoryKeywordDao {
	public void insertKeywords(Map<String, String> keywordMap, String categoryId) {
		String query = Query.INSERT_INTO_CATEGORY_KEYWORDS;

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			for (Map.Entry<String, String> entry : keywordMap.entrySet()) {
				preparedStatement.setString(1, entry.getKey());
				preparedStatement.setString(2, entry.getValue());
				preparedStatement.setString(3, categoryId);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	public Map<String, String> getKeywordsByCategoryId(String categoryId) {
		String query = Query.SELECT_CATEGORIES_BY_ID;
		Map<String, String> keywordMap = new HashMap<String, String>();

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, categoryId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				keywordMap.put(resultSet.getString("keyword_id"), resultSet.getString("keyword"));
			}
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}

		return keywordMap;
	}
}
