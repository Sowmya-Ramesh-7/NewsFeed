package com.newsfeed.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class JwtUtil {
    private static final String TOKEN_FILE_PATH = "token_store.txt";

    public static void saveToken(String token) {
        try (FileWriter writer = new FileWriter(TOKEN_FILE_PATH)) {
            writer.write(token);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static Optional<String> getToken() {
        try {
            String token = Files.readString(Paths.get(TOKEN_FILE_PATH)).trim();
            return Optional.of(token);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return Optional.empty();
        }
    }
}
 