package com.example.service;

import com.example.message.ErrorMessage;

import static com.example.message.ErrorMessage.ENTITY_NOT_FOUND;

public class MessageGenerator {

    public static String generateMessage(ErrorMessage errorMessage, Object... params) {
        return errorMessage.getMessage().formatted(params);
    }

    public static String generateEntityNotFoundMessage(Object param) {
        return ENTITY_NOT_FOUND.getMessage().formatted(param);
    }
}
