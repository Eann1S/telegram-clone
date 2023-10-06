package com.example.dto.response;

public record UserDto(
        Long id,
        String email,
        String username
) {

    public static UserDto of(Long id, String username, String email) {
        return new UserDto(id, username, email);
    }
}
