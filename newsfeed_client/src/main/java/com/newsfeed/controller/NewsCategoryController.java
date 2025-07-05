package com.newsfeed.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.newsfeed.model.NewsCategory;
import com.newsfeed.service.NewsCategoryService;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;
import com.newsfeed.util.constants.Prompts;

public class NewsCategoryController {
	private NewsCategoryService newsCategoryService;
	private Map<String, NewsCategory> allCategories;
	
	public NewsCategoryController(NewsCategoryService newsCategoryService) {
		this.newsCategoryService = newsCategoryService;
		this.allCategories = new HashMap<String, NewsCategory>();
	}

	public void addNewsCategory() {}

	public void displayAllCategories() throws IOException, InterruptedException {
		if(allCategories.size() == 0) {
			getAllCategories();
		}
		
		allCategories.forEach((categoryId, newsCategory)->{
			System.out.println(categoryId + ". " + newsCategory.getCategory());
		});
	}
	
	public Map<String, NewsCategory> getAllCategories() throws IOException, InterruptedException {
		allCategories = newsCategoryService.getAllCategories();
		return allCategories;
	}

	public void getCategoryById() {
		
	}

	public String getCategoryInputFromUser() throws IOException, InterruptedException {
		System.out.println("\n"+Prompts.PLEASE_CHOOSE_OPTION);
		displayAllCategories();
		String categoryId = InputUtil.readLine(Prompts.ENTER_YOUR_CHOICE);
		if(allCategories.containsKey(categoryId)) {
			return allCategories.get(categoryId).getCategory();
		}
		throw new IllegalArgumentException(Messages.INVALID_CATEGORY_ID);
	}

}
