package com.newsfeed.dao;

import com.newsfeed.exception.ServerException;
import com.newsfeed.model.ExternalServer;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Query;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ExternalServerDao {
	
    public Optional<ExternalServer> getByName(String name) {
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.GET_EXTERNAL_SERVER_BY_NAME)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapResultSetToServer(resultSet));
            }

        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new ServerException(Messages.DATABASE_ERROR);
        }
        return Optional.empty();
    }

    public List<ExternalServer> getAll() {
        List<ExternalServer> servers = new ArrayList<>();

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.GET_ALL_EXTERNAL_SERVERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                servers.add(mapResultSetToServer(resultSet));
            }

        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new ServerException(Messages.DATABASE_ERROR);
        }

        return servers;
    }

    public int update(String name, String columnName, Object value) {
        String query = "UPDATE external_server SET " + columnName + " = ? WHERE api_name = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (value instanceof String) {
                preparedStatement.setString(1, (String) value);
            } else if (value instanceof Boolean) {
                preparedStatement.setBoolean(1, (Boolean) value);
            } else if (value instanceof LocalDateTime) {
                preparedStatement.setTimestamp(1, Timestamp.valueOf((LocalDateTime) value));
            }

            preparedStatement.setString(2, name);
            return preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new ServerException(Messages.DATABASE_ERROR);
        }
    }

    private ExternalServer mapResultSetToServer(ResultSet rs) throws SQLException {
        ExternalServer server = new ExternalServer();
        server.setServerId(rs.getString("server_id"));
        server.setApiName(rs.getString("api_name"));
        server.setBaseUrl(rs.getString("base_url"));
        server.setApiKey(rs.getString("api_key"));
        Timestamp timestamp = rs.getTimestamp("last_accessed");
        server.setLastAccessed(timestamp != null ? timestamp.toLocalDateTime() : null);
        server.setActive(rs.getBoolean("is_active"));
        return server;
    }
}
