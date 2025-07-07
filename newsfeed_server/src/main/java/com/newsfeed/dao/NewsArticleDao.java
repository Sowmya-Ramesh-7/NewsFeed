package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.model.NewsArticle;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
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
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	public List<NewsArticle> getArticlesByDateRange(LocalDate start, LocalDate end) {
		return getArticlesByFilters(start, end, null);
	}

	public List<NewsArticle> getArticlesByCategory(String category) {
		return getArticlesByFilters(null, null, category);
	}

	public List<NewsArticle> getArticlesByFilters(LocalDate start, LocalDate end, String category) {
		List<Object> parameters = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT * FROM news_articles WHERE is_hidden = false ");

		if (start != null && end != null) {
			query.append(" AND published_date BETWEEN ? AND ?");
			parameters.add(java.sql.Date.valueOf(start));
			parameters.add(java.sql.Date.valueOf(end));
		}

		if (category != null && !category.isBlank()) {
			query.append(" AND category_id = ?");
			parameters.add(category);
		}

		query.append(" ORDER BY published_date DESC");

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
			for (int i = 0; i < parameters.size(); i++) {
				preparedStatement.setObject(i + 1, parameters.get(i));
			}

			List<NewsArticle> articles = new ArrayList<>();
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				NewsArticle article = getArticlefromResultSet(resultSet);
				articles.add(article);
			}
			return articles;

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

    public List<NewsArticle> getPersonalizedHistory(String userId) {
        List<NewsArticle> articles = new ArrayList<>();

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.FETCH_NEWS_HISTORY_BASED_ON_PRIORITY)) {

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userId);
            preparedStatement.setString(3, userId);
            preparedStatement.setString(4, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	NewsArticle article = getArticlefromResultSet(resultSet);
				articles.add(article);
            }

        } catch (SQLException | ClassNotFoundException | IOException exception) {
            throw new ServerException(Messages.DATABASE_ERROR);
        }

        return articles;
    }

	public List<NewsArticle> getArticlesByText(String keyword) {
		List<NewsArticle> articles = new ArrayList<>();
		String query = Query.SEARCH_BY_ARTILCES_KEYWORD;

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, keyword);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				NewsArticle article = getArticlefromResultSet(resultSet);
				articles.add(article);
			}

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}
		return articles;
	}

	private NewsArticle getArticlefromResultSet(ResultSet resultSet) throws SQLException {
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
		return article;
	}

	public boolean hideArticleById(String articleId) {
		String query = Query.HIDE_ARTICLE_BY_ID;
		return executeUpdate(query, articleId);
	}

	public boolean hideArticlesByCategory(String categoryId) {
		String query = Query.HIDE_ARTICLE_BY_CATEGORY_ID;
		return executeUpdate(query, categoryId);
	}

	public boolean hideArticlesByKeyword(String keyword) {
		String query = Query.HIDE_ARTICLE_BY_KEYWORD;
		return executeUpdate(query, keyword);
	}

	private boolean executeUpdate(String query, String value) {
		int updatedCount = 0;
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, value);
			updatedCount = preparedStatement.executeUpdate();
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}

		return updatedCount > 0;
	}

}
