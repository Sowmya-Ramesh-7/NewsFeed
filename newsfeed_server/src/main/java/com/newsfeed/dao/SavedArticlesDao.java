package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.util.IdGenerator;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SavedArticlesDao {

	public boolean saveArticle(String userId, String articleId) {
		String query = Query.INSERT_INTO_SAVED_ARTICLES;

		try (Connection conn = DBConnect.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			String id = IdGenerator.generate("SAVED");
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, userId);
			preparedStatement.setString(3, articleId);
			preparedStatement.executeUpdate();
			return true;

		} catch (SQLException exception) {
			if (exception.getSQLState().equals("23000")) {
				return false;
			}
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		} catch (ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	public List<NewsArticle> getSavedArticles(String userId) {
		String query = Query.GET_ALL_SAVED_ARTICLES;
		List<NewsArticle> articles = new ArrayList<>();

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				NewsArticle article = new NewsArticle();
				article.setArticleId(resultSet.getString("article_id"));
				article.setTitle(resultSet.getString("title"));
				article.setContent(resultSet.getString("content"));
				article.setSource(resultSet.getString("source_link"));
				article.setArticleUrl(resultSet.getString("article_url"));
				article.setCategoryId(resultSet.getString("category_id"));
				article.setImageUrl(resultSet.getString("image_url"));
				Timestamp timestamp = resultSet.getTimestamp("published_date");
				if (timestamp != null) {
					article.setPublishedDate(timestamp.toLocalDateTime());
				}
				articles.add(article);
			}

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}

		return articles;
	}
}
