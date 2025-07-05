package com.newsfeed.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.newsfeed.dao.UserDao;
import com.newsfeed.model.User;
import com.newsfeed.util.IdGenerator;
import com.newsfeed.util.JwtUtil;

public class UserAuthenticationService {
	private final UserDao userDao;

	public UserAuthenticationService(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public String signup(User user) {
		Optional<User> userOptional = userDao.findByEmail(user.getEmailAddress());
		
		if(userOptional.isPresent()) {
			return "";
		}
		user.setIsAdmin(false);
		user.setUserId(IdGenerator.generate("User"));
		userDao.add(user);
		return user.getUserId();
	}
	
	public Map<String,String> login(String email, String password) {
        Optional<User> existingUser = userDao.findByEmail(email);
        Map<String, String> loginResponse = new HashMap<String,String>();
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(password)) {
        	User user = existingUser.get();
        	String token = JwtUtil.generateToken(user);
        	loginResponse.put("token", token);
        	loginResponse.put("isAdmin", String.valueOf(user.getIsAdmin()));
        	loginResponse.put("name", user.getName());
        }
        return loginResponse;
    }
}
