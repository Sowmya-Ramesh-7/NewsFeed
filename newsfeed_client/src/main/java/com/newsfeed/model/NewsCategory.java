package com.newsfeed.model;

import java.util.Map;

public class NewsCategory {
    private String categoryId;
    private String category;
    private Map<String, String> keywordsMap;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, String> getKeywordsMap() {
        return keywordsMap;
    }

    public void setKeywordsMap(Map<String, String> keywordsMap) {
        this.keywordsMap = keywordsMap;
    }
}

