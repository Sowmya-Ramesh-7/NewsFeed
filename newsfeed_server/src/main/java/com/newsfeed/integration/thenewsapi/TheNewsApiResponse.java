package com.newsfeed.integration.thenewsapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.newsfeed.model.NewsArticle;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TheNewsApiResponse {
    private List<NewsArticle> data;

    public List<NewsArticle> getData() {
        return data;
    }

    public void setData(List<NewsArticle> data) {
        this.data = data;
    }
}
