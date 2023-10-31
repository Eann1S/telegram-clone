package com.example.message;

import lombok.Getter;

public enum ErrorMessage {

    USER_NOT_FOUND("User %s does not exist");

    @Getter
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String formatWith(Object property) {
        return message.formatted(property);
    }
}
