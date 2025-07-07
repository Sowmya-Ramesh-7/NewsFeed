package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArticleReactionDao {

	public String getUserReaction(String userId, String articleId) {
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.GET_USER_REACTION)) {

			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, articleId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getString("reaction_type");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}

		return "";
	}

	public boolean saveOrUpdateReaction(String userId, String articleId, String newReaction) {
		String existingReaction = getUserReaction(userId, articleId);

		if (newReaction.equalsIgnoreCase(existingReaction)) {
			return false;
		}

		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.UPSERT_USER_REACTION)) {

			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, articleId);
			preparedStatement.setString(3, newReaction.toUpperCase());
			preparedStatement.executeUpdate();

			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	public int getReportCountForArticle(String articleId) {
		String query = Query.SELECT_COUNT_OF_REPORT_ARTICLE;
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatment = connection.prepareStatement(query)) {
			preparedStatment.setString(1, articleId);
			ResultSet resultSet = preparedStatment.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
