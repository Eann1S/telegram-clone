package com.example.dto.response;

public record ErrorDto(
        String errorMessage,
        Long timestamp
) {

    public static ErrorDto of(String errorMessage, Long timestamp) {
        return new ErrorDto(errorMessage, timestamp);
    }
}
