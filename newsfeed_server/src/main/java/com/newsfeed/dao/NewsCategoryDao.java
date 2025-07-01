package com.newsfeed.dao;

import com.newsfeed.model.NewsCategory;
import com.newsfeed.util.constants.Query;
import com.newsfeed.exception.ServerException;
import com.newsfeed.util.constants.Messages;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class NewsCategoryDao {

    public Map<String, NewsCategory> getAllCategories() {
        Map<String, NewsCategory> categories = new HashMap<String, NewsCategory>();

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.GET_ALL_NEWS_CATEGORIES_WITH_KEYWORD);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
            	String categoryId = resultSet.getString("category_id");
            	NewsCategory category = categories.getOrDefault(categoryId, new NewsCategory());
                
                category.setCategoryId(categoryId);
                category.setCategory(resultSet.getString("category"));
                Map<String, String> keywordMap = category.getKeywordsMap();
                if(keywordMap == null) {
                	keywordMap = new HashMap<String, String>();
                }
                
                String keyword = resultSet.getString("keyword");
                String keywordId = resultSet.getString("keyword_id");

                if (keyword != null && keywordId != null) {
                    keywordMap.put(keywordId, keyword);
                }
                category.setKeywordsMap(keywordMap);
                categories.put(categoryId, category);
                
            }

        } catch (SQLException | ClassNotFoundException | IOException e) {
        	e.printStackTrace();
            throw new ServerException(Messages.DATABASE_ERROR);
        }

        return categories;
    }
}


