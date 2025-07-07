package com.newsfeed.controller;

import com.newsfeed.model.ExternalServer;
import com.newsfeed.service.ExternalServerService;
import com.newsfeed.util.InputUtil;
import com.newsfeed.util.constants.Messages;

import java.io.IOException;
import java.util.List;

public class ExternalServerController {
	private ExternalServerService externalServerService;

	public ExternalServerController(ExternalServerService externalServerService) {
		this.externalServerService = externalServerService;
	}

	public void getAllServersWithStatus() throws IOException, InterruptedException {
		List<ExternalServer> servers = externalServerService.getAllServers();

		if (servers.isEmpty()) {
			System.out.println("No external servers configured.");
			return;
		}

		System.out.println(Messages.LIST_OF_SERVERS);
		for (ExternalServer server : servers) {
			String status = server.isActive() ? "Active" : "Not Active";
			System.out.println(server.getServerId() + ". " + server.getApiName() + " - " + status + " | Last accessed: "
					+ server.getLastAccessed());
		}
	}

	public void getAllServerDetails() throws IOException, InterruptedException {
		List<ExternalServer> servers = externalServerService.getAllServers();
		if (!servers.isEmpty()) {
			System.out.println("List of external server details:");
			servers.forEach((server) -> {
				System.out.println(server.getServerId() + ". " + server.getApiName() + " - " + server.getApiKey());
			});
		}
	}

	public void updateServerDetails() throws IOException, InterruptedException {
		String serverId = InputUtil.readLine(Messages.ENTER_SERVER_ID);
		String updatedApiKey = InputUtil.readLine(Messages.ENTER_UPDATED_API_KEY);
		ExternalServer server = new ExternalServer();
		server.setApiKey(updatedApiKey);
		server.setServerId(serverId);
		externalServerService.updateServerDetails(server);

	}
}
