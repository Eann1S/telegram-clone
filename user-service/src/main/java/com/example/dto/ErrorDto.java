package com.example.dto;

public record ErrorDto(
        String errorMessage
) {

    public static ErrorDto of(String errorMessage) {
        return new ErrorDto(errorMessage);
    }
}
