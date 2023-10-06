package com.example.dto.request;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        @Email(message = "{email.valid}")
        String email,
        String username
) {

    public static UpdateUserRequest of(String email, String username) {
        return new UpdateUserRequest(email, username);
    }
}
