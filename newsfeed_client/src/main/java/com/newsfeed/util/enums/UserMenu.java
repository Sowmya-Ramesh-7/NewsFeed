package com.newsfeed.util.enums;

import com.newsfeed.util.constants.Messages;

public enum UserMenu {
    HEADLINES("1", "Headlines"),
    SAVED_ARTICLES("2", "Saved Articles"),
    SEARCH("3", "Search"),
    NOTIFICATIONS("4", "Notifications"),
    LOGOUT("5", "Logout");

    private final String optionNumber;
    private final String description;

    UserMenu(String optionNumber, String description) {
        this.optionNumber = optionNumber;
        this.description = description;
    }

    public String getOptionNumber() {
        return optionNumber;
    }

    public String getDescription() {
        return description;
    }

    public static UserMenu getByOptionNumber(String optionNumber) {
        for (UserMenu option : values()) {
            if (option.optionNumber.equals(optionNumber)) {
                return option;
            }
        }
        throw new IllegalArgumentException(Messages.INVALID_OPTION);
    }
}
