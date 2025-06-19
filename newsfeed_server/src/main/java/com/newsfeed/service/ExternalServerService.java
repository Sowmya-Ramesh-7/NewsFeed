package com.newsfeed.service;

import com.newsfeed.dao.ExternalServerDao;
import com.newsfeed.model.ExternalServer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ExternalServerService {

    private final ExternalServerDao serverDao;

    public ExternalServerService(ExternalServerDao serverDao) {
        this.serverDao = serverDao;
    }

    public Optional<ExternalServer> findByApiName(String apiName) {
        return serverDao.getByName(apiName);
    }

    public List<ExternalServer> findAll() {
        return serverDao.getAll();
    }
    
    public void updateLastAccessed(String apiName) {
        serverDao.update(apiName, "last_accessed", LocalDateTime.now());
    }

    public void updateActiveStatus(String apiName, boolean isActive) {
        serverDao.update(apiName, "is_active", isActive);
    }
    
    public void updateActiveKey(String apiName, String value) {
        serverDao.update(apiName, "api_key", value);
    }
}
