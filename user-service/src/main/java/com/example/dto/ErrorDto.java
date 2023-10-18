package com.example.dto;

public record ErrorDto(
        String errorMessage,
        Long timestamp
) {

    public static ErrorDto of(String errorMessage, Long timestamp) {
        return new ErrorDto(errorMessage, timestamp);
    }
}
