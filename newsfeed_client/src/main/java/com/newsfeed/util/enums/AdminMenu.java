package com.newsfeed.util.enums;

import com.newsfeed.util.constants.Messages;

public enum AdminMenu {
	VIEW_ALL_SERVER_STATUS("1", "View list of external servers and status"),
	VIEW_ALL_SERVER_DETAILS("2", "View external server details"),
	EDIT_SERVER_DETAILS("3", "Update/Edit external server details"), 
	ADD_CATEGORY("4", "Add new News Category"),
	VIEW_NOTIFICATION("5", "View Notifications"),
	HIDE_ARTICLES("6", "Hide News Articles"),
	LOGOUT("7", "Logout");

	private final String optionNumber;
	private final String description;

	AdminMenu(String optionNumber, String description) {
		this.optionNumber = optionNumber;
		this.description = description;
	}

	public String getOptionNumber() {
		return optionNumber;
	}

	public String getDescription() {
		return description;
	}

	public static AdminMenu getByOptionNumber(String optionNumber) {
		for (AdminMenu option : AdminMenu.values()) {
			if (option.optionNumber.equals(optionNumber)) {
				return option;
			}
		}
		throw new IllegalArgumentException(Messages.INVALID_OPTION);
	}
}
