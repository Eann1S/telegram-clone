package com.example.message;

import lombok.Getter;

public enum ErrorMessage {

    ENTITY_NOT_FOUND("Entity with property %s does not exist");

    @Getter
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
