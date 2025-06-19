package com.newsfeed.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.dto.ApiResponse;
import com.newsfeed.util.ApplicationContext;
import com.newsfeed.util.JwtUtil;
import com.newsfeed.util.constants.Messages;

import io.jsonwebtoken.Claims;

@WebFilter(urlPatterns = { "/*" })
public class JwtAuthenticationFilter implements Filter {

	private static final Set<String> EXCLUDED_PATHS = Set.of("/auth/login", "/auth/signup");
	private static final String ADMIN_PATH_PREFIX = "/admin/servers";
	private ObjectMapper objectMapper;

	@Override
	public void init(FilterConfig filterConfig) {
		objectMapper = ApplicationContext.getObject(ObjectMapper.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String path = httpRequest.getRequestURI().replaceFirst(httpRequest.getContextPath(), "");

		try {
			if (isExcludedPath(path)) {
				chain.doFilter(request, response);
				return;
			}

			String authHeader = httpRequest.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				sendUnauthorized(httpResponse);
				return;
			}

			Claims claims = JwtUtil.validateTokenAndGetSubject(authHeader.substring(7));
			String userId = claims.getSubject();
			boolean isAdmin = Boolean.parseBoolean(String.valueOf(claims.get("isAdmin")));

			if (isAdminRoute(path) && !isAdmin) {
				sendForbidden(httpResponse, Messages.FORBIDDEN_REQUEST);
				return;
			}

			httpRequest.setAttribute("userId", userId);
			chain.doFilter(request, response);

		} catch (Exception e) {
			sendUnauthorized(httpResponse);
		}
	}

	private boolean isExcludedPath(String path) {
		return EXCLUDED_PATHS.contains(path);
	}

	private boolean isAdminRoute(String path) {
		return path.startsWith(ADMIN_PATH_PREFIX);
	}

	private void sendUnauthorized(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		objectMapper.writeValue(response.getWriter(), ApiResponse.error(Messages.UNAUTHORIZED));
	}

	private void sendForbidden(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		objectMapper.writeValue(response.getWriter(), ApiResponse.error(message));
	}
}
