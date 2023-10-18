package com.example.message;

import lombok.Getter;

public enum ErrorMessage {

    MESSAGE_NOT_FOUND("In partition {%s:%s} message %s does not exist");

    @Getter
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String formatWith(Object... properties) {
        return message.formatted(properties);
    }
}
