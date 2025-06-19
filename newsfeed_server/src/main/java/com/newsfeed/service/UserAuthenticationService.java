package com.newsfeed.service;

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
	
	public Optional<String> login(String email, String password) {
        Optional<User> existingUser = userDao.findByEmail(email);

        if (existingUser.isPresent() && existingUser.get().getPassword().equals(password)) {
            String token = JwtUtil.generateToken(existingUser.get());
            return Optional.of(token);
        }

        return Optional.empty();
    }
}
