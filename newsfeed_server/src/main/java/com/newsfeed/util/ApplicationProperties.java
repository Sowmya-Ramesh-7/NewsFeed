package com.newsfeed.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.newsfeed.exception.ServerException;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Property;

public class ApplicationProperties {
	private static final Properties properties = new Properties();
    private static final String APPLICATION_PROPERTIES_FILE = Property.APPLICATION_PROPERTIES_FILE_PATH;
    
    private ApplicationProperties() {
    }

    static {
        try (FileInputStream input = new FileInputStream(APPLICATION_PROPERTIES_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new ServerException(Messages.FAILED_LOAD_DATA_FROM_SERVER);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
