package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class ArticleHistoryDao {

	public void addReadHistory(String userId, List<String> articleIds) {
		String query = Query.INSERT_INTO_ARTICLE_READ_HISTORY;

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			for (String articleId : articleIds) {
				preparedStatement.setString(1, userId);
				preparedStatement.setString(2, articleId);
				preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}
}
