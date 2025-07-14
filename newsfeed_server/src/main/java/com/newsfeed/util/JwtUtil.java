package com.newsfeed.util;

import com.newsfeed.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
	private static final String SECRET = ApplicationProperties.get("jwt.secret");
	private static final long EXPIRATION_TIME_MS = Long.valueOf(ApplicationProperties.get("jwt.expiration"));

	public static String generateToken(User user) {
		return Jwts.builder()
				.setSubject(user.getUserId())
				.claim("email", user.getEmailAddress())
				.claim("isAdmin", user.getIsAdmin())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
				.signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
				.compact();
	}
	
	public static Claims validateTokenAndGetSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}