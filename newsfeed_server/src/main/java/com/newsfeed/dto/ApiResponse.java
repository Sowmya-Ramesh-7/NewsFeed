package com.newsfeed.dto;

public class ApiResponse {

	private final boolean success;
	private final String message;
	private final Object data;

	private ApiResponse(boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public static ApiResponse success(String message, Object data) {
		return new ApiResponse(true, message, data);
	}

	public static ApiResponse success(String message) {
		return new ApiResponse(true, message, null);
	}

	public static ApiResponse error(String message) {
		return new ApiResponse(false, message, null);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}
}
