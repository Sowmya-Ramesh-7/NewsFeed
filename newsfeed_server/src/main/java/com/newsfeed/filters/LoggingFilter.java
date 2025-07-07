package com.newsfeed.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = { "/*" })
public class LoggingFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String method = req.getMethod();
		String uri = req.getRequestURI();
		String query = req.getQueryString() != null ? "?" + req.getQueryString() : "";
		String fullUrl = method + " " + uri + query;

		long start = System.currentTimeMillis();
		logger.info(() -> String.format("START %s ", fullUrl));

		try {
			chain.doFilter(request, response);
			long duration = System.currentTimeMillis() - start;
			logger.info(() -> String.format("END %s - Duration: %d ms", fullUrl, duration));
		} catch (Exception exception) {
			logger.log(Level.SEVERE,
					String.format("ERROR %s - %s: %s", fullUrl, exception.getClass().getSimpleName(), exception.getMessage()), exception);
			throw exception;
		}
	}
}
