package com.example.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String errorMessage,
        Integer statusCode,
        Long timestamp
) {

    public static ErrorResponse of(String errorMessage, Integer statusCode, Long timestamp) {
        return ErrorResponse.builder()
                .errorMessage(errorMessage)
                .statusCode(statusCode)
                .timestamp(timestamp)
                .build();
    }
}
