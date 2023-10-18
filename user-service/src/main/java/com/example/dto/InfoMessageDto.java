package com.example.dto;

import com.example.message.InfoMessage;

public record InfoMessageDto(
        String message
) {

    public static InfoMessageDto of(InfoMessage infoMessage) {
        return new InfoMessageDto(infoMessage.getMessage());
    }
}
