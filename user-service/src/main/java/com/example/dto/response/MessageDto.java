package com.example.dto.response;

import com.example.message.InfoMessage;

public record MessageDto(
        String message
) {

    public static MessageDto of(InfoMessage infoMessage) {
        return new MessageDto(infoMessage.getMessage());
    }
}
