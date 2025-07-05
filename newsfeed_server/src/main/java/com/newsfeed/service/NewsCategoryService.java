package com.newsfeed.service;

import com.newsfeed.dao.NewsCategoryDao;
import com.newsfeed.model.NewsCategory;

import java.util.Map;

public class NewsCategoryService {

    private final NewsCategoryDao categoryDao;

    public NewsCategoryService(NewsCategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public Map<String, NewsCategory> getAllCategories() {
        return categoryDao.getAllCategories();
    }
}
