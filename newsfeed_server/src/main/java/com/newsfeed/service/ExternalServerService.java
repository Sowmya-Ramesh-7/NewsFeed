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

    public Optional<ExternalServer> findById(int Id) {
        return serverDao.getById(Id);
    }

    public List<ExternalServer> findAll() {
        return serverDao.getAll();
    }
    
    public void updateLastAccessed(int serverId) {
        serverDao.update(serverId, "last_accessed", LocalDateTime.now());
    }

    public void updateActiveStatus(int serverId, boolean isActive) {
        serverDao.update(serverId, "is_active", isActive);
    }
    
    public void updateActiveKey(int serverId, String value) {
        serverDao.update(serverId, "api_key", value);
    }
}
