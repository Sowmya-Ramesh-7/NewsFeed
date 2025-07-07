package com.newsfeed.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.model.ApiResponse;
import com.newsfeed.model.ExternalServer;
import com.newsfeed.util.constants.ApiRoutes;
import com.newsfeed.util.constants.Messages;

public class ExternalServerService {
	private ObjectMapper objectMapper;
	private HttpClient httpClient;

	public ExternalServerService(ObjectMapper objectMapper, HttpClient httpClient) {
		this.objectMapper = objectMapper;
		this.httpClient = httpClient;
	}

	public List<ExternalServer> getAllServers() throws IOException, InterruptedException {
		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
		HttpRequest request = HttpRequestBuilder.buildRequest("GET", ApiRoutes.SERVER_ROUTE, headers);
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

		if (response.statusCode() == 200) {
			String dataJson = objectMapper.writeValueAsString(apiResponse.getData());
			List<ExternalServer> servers = objectMapper.readValue(dataJson, new TypeReference<List<ExternalServer>>() {});
			System.out.println(apiResponse.getMessage());
			if (!servers.isEmpty()) {
				return servers;
			}
			System.out.println(Messages.NO_SERVER_CONFIGURED);
		} else {
			System.out.println(apiResponse.getMessage());
		}
		return Collections.emptyList();
	}

	public Optional<ExternalServer> getServerById(int serverId) throws InterruptedException, IOException {
		String url = ApiRoutes.SERVER_ROUTE + "/" + serverId;
		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
		HttpRequest request = HttpRequestBuilder.buildRequest("GET", url, headers);

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

		if (response.statusCode() == 200) {
			Object data = apiResponse.getData();
			ExternalServer server = objectMapper.convertValue(data, ExternalServer.class);
			return Optional.ofNullable(server);
		} else {
			System.out.println(apiResponse.getMessage());
			return Optional.empty();
		}
	}

	public void updateServerDetails(ExternalServer server) throws IOException, InterruptedException {
		String url = ApiRoutes.SERVER_ROUTE + "/" + server.getServerId() + "?apiKey=" + server.getApiKey();

		Map<String, String> headers = HttpRequestBuilder.getAuthHeader();
		HttpRequest request = HttpRequestBuilder.buildRequest("PUT", url, headers);

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

		System.out.println(apiResponse.getMessage());
	}

}
