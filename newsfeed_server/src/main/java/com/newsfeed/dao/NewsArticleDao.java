package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class NewsArticleDao {

	public void addAll(List<NewsArticle> articles) {
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.INSERT_INTO_NEWS_ARTICLES)) {
			for (NewsArticle article : articles) {
				preparedStatement.setString(1, article.getArticleId());
				preparedStatement.setString(2, article.getTitle());
				preparedStatement.setString(3, article.getContent());
				preparedStatement.setString(4, article.getSource());
				preparedStatement.setString(5, article.getArticleUrl());
				preparedStatement.setString(6, article.getCategoryId());
				preparedStatement.setString(7, article.getImageUrl());
				preparedStatement.setTimestamp(8, Timestamp.valueOf(article.getPublishedDate()));
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}
}
