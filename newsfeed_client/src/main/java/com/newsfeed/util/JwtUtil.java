package com.newsfeed.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class JwtUtil {
	private static final String TOKEN_FILE_PATH = "token_store.txt";

	public static void saveToken(String token) {
		try {
			ensureTokenFileExists();
			try (FileWriter writer = new FileWriter(TOKEN_FILE_PATH)) {
				writer.write(token);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static Optional<String> getToken() {
		try {
			ensureTokenFileExists();
			String token = Files.readString(Paths.get(TOKEN_FILE_PATH)).trim();
			return Optional.of(token);
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return Optional.empty();
		}
	}

	public static void clearToken() {
		try {
			ensureTokenFileExists();
			try (FileWriter writer = new FileWriter(TOKEN_FILE_PATH)) {
				writer.write("");
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private static void ensureTokenFileExists() throws IOException {
		File tokenFile = new File(TOKEN_FILE_PATH);
		if (!tokenFile.exists()) {
			tokenFile.createNewFile();
		}
	}
}
