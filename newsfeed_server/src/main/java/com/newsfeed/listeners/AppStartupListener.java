package com.newsfeed.listeners;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.newsfeed.integration.ThirdPartyNewsScheduler;
import com.newsfeed.util.ApplicationContext;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@WebListener
public class AppStartupListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        this.scheduler = Executors.newScheduledThreadPool(2);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        ApplicationContext.registerInstance(ScheduledExecutorService.class, scheduler);
        ApplicationContext.registerInstance(HttpClient.class, HttpClient.newHttpClient());
        ApplicationContext.registerInstance(ObjectMapper.class, objectMapper);

        ThirdPartyNewsScheduler newsScheduler = ApplicationContext.getObject(ThirdPartyNewsScheduler.class);
        newsScheduler.scheduleNewsFetchTasks();
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
