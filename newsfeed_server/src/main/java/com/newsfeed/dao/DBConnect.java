package com.newsfeed.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.newsfeed.util.constants.Property;

public class DBConnect {
	static String dbConfigFilePath = Property.DB_PROPERTIES;

	public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {

		FileInputStream in = new FileInputStream(dbConfigFilePath);
		Properties dbConfigProperties = new Properties();
		dbConfigProperties.load(in);

		Class.forName(dbConfigProperties.getProperty("driverClassName"));

		Connection connection = DriverManager.getConnection(
				dbConfigProperties.getProperty("url"),
				dbConfigProperties.getProperty("username"), 
				dbConfigProperties.getProperty("password")
			);
		return connection;
	}
}
