package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.model.User;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

	public boolean add(User user) {
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.INSERT_USER)) {

			preparedStatement.setString(1, user.getUserId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setLong(3, user.getPhoneNumber());
			preparedStatement.setString(4, user.getEmailAddress());
			preparedStatement.setBoolean(5, user.getIsAdmin());
			preparedStatement.setString(6, user.getPassword());

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}
	}

	public Optional<User> findByEmail(String email) {
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.SELECT_BY_EMAIL)) {

			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getString("user_id"));
				user.setName(resultSet.getString("name"));
				user.setPhoneNumber(resultSet.getLong("phone_number"));
				user.setEmailAddress(resultSet.getString("email_address"));
				user.setIsAdmin(resultSet.getBoolean("is_admin"));
				user.setPassword(resultSet.getString("password"));
				return Optional.of(user);
			}

		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}

		return Optional.empty();
	}

	public List<User> getAllAdmins() {
		List<User> admins = new ArrayList<User>();
		try (Connection connection = DBConnect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(Query.SELECT_ADMINS)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getString("user_id"));
				user.setName(resultSet.getString("name"));
				user.setPhoneNumber(resultSet.getLong("phone_number"));
				user.setEmailAddress(resultSet.getString("email_address"));
				user.setIsAdmin(resultSet.getBoolean("is_admin"));
				admins.add(user);
			}
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			throw new ServerException(Messages.DATABASE_ERROR);
		}

		return admins;
	}
}
