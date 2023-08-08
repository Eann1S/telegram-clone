package com.example.dto.response;

import lombok.Builder;

@Builder
public record UserDto(
        String id,
        String username,
        String email,
        String phoneNumber
) {

    public static UserDto of(String id, String username, String email, String phoneNumber) {
        return new UserDto(id, username, email, phoneNumber);
    }
}
