package com.newsfeed.util.enums;

import com.newsfeed.util.constants.Messages;

public enum MainMenu {
    SIGNUP("1", "Signup"),
    LOGIN("2", "Login"),
    EXIT("3", "Exit");

    private final String optionNumber;
    private final String description;

    MainMenu(String optionNumber, String description) {
        this.optionNumber = optionNumber;
        this.description = description;
    }

    public String getOptionNumber() {
        return optionNumber;
    }

    public String getDescription() {
        return description;
    }

    public static MainMenu getByOptionNumber(String optionNumber) {
        for (MainMenu option : values()) {
            if (option.optionNumber.equals(optionNumber)) {
                return option;
            }
        }
        throw new IllegalArgumentException(Messages.INVALID_OPTION);
    }
}
