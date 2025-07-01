package com.newsfeed.main;

import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.newsfeed.controller.HomeController;
import com.newsfeed.util.ApplicationContext;

public class NewsfeedApplication {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		ApplicationContext.registerInstance(ObjectMapper.class, objectMapper);
		ApplicationContext.registerInstance(HttpClient.class, HttpClient.newHttpClient());

		try {
			boolean isExit = false;

			while (!isExit) {
				isExit = HomeController.home();
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
}
