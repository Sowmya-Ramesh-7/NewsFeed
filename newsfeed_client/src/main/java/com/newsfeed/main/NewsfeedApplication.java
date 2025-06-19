package com.newsfeed.main;

import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.SQLException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.controller.HomeController;
import com.newsfeed.util.ApplicationContext;

public class NewsfeedApplication {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		ApplicationContext.registerInstance(ObjectMapper.class, new ObjectMapper());
		ApplicationContext.registerInstance(HttpClient.class, HttpClient.newHttpClient());
		
		try (Scanner scanner = new Scanner(System.in)) {
			
			boolean isExit = false;

			while (!isExit) {
				isExit = HomeController.home(scanner);
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
}

