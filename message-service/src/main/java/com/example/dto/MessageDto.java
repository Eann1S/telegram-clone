package com.example.dto;

import java.time.LocalDateTime;

public record MessageDto(
        String messageId,
        UserDto sender,
        UserDto receiver,
        String text,
        LocalDateTime sendTime
) {
}
