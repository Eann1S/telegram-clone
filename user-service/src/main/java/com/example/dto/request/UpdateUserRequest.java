package com.example.dto.request;

import lombok.Builder;

@Builder
public record UpdateUserRequest(
        String username,
        String email,
        String phoneNumber
) {

    public static UpdateUserRequest of(String username, String email, String phoneNumber) {
        return UpdateUserRequest.builder()
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }
}
