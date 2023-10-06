package com.example.message;

import lombok.Getter;

public enum InfoMessage {

    USER_UPDATED("Your data was successfully updated");

    @Getter
    private final String message;

    InfoMessage(String message) {
        this.message = message;
    }
}
