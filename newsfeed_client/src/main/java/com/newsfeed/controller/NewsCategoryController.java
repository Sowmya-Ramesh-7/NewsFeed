package com.newsfeed.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.newsfeed.model.NewsCategory;
import com.newsfeed.service.NewsCategoryService;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Patterns;
import com.newsfeed.util.constants.Prompts;

public class NewsCategoryController {
	private NewsCategoryService newsCategoryService;
	private Map<String, NewsCategory> allCategories;

	public NewsCategoryController(NewsCategoryService newsCategoryService) {
		this.newsCategoryService = newsCategoryService;
		this.allCategories = new HashMap<String, NewsCategory>();
	}

	public void addNewsCategory() throws IOException, InterruptedException {
		if (allCategories.size() == 0) {
			getAllCategories();
		}

		String categoryName = InputUtil.readLine(Prompts.ENTER_CATEGORY_NAME, Patterns.NAME_PATTERN,
				Messages.INVALID_CATEGORY);
		List<NewsCategory> existingCategory = allCategories.values().stream()
				.filter(category -> category.getCategory().equalsIgnoreCase(categoryName)).collect(Collectors.toList());

		if (!existingCategory.isEmpty()) {
			System.out.println(Messages.CATEGORY_ALREADY_EXISTS);
			return;
		}

		String keywordsInput = InputUtil.readLine(Prompts.ENTER_KEYWORDS_FOR_CATEGORY);
		String[] keywords = keywordsInput.split(",");
		Map<String, String> keywordsMap = new HashMap<String, String>();
		if (!keywordsInput.isBlank()) {
			for (int index = 0; index < keywords.length; index++) {
				String id = "Keyword_" + (int) (Math.random() * 10000);
				keywordsMap.put(id, keywords[index].trim());
			}
		}

		NewsCategory category = new NewsCategory();
		category.setCategoryId("CATEGORY_" + (allCategories.size() + 1));
		category.setCategory(categoryName);
		category.setKeywordsMap(keywordsMap);
		if (newsCategoryService.addCategory(category)) {
			allCategories.put(category.getCategoryId(), category);
		}
	}

	public void displayAllCategories() throws IOException, InterruptedException {
		if (allCategories.size() == 0) {
			getAllCategories();
		}

		allCategories.forEach((categoryId, newsCategory) -> {
			System.out.println(categoryId + ". " + newsCategory.getCategory());
		});
	}

	public Map<String, NewsCategory> getAllCategories() throws IOException, InterruptedException {
		allCategories = newsCategoryService.getAllCategories();
		return allCategories;
	}

	public String getCategoryInputFromUser() throws IOException, InterruptedException {
		System.out.println("\n" + Prompts.PLEASE_CHOOSE_OPTION);
		displayAllCategories();
		String categoryId = InputUtil.readLine(Prompts.ENTER_YOUR_CHOICE);
		if (allCategories.containsKey(categoryId)) {
			return allCategories.get(categoryId).getCategoryId();
		}
		throw new IllegalArgumentException(Messages.INVALID_CATEGORY_ID);
	}

}
