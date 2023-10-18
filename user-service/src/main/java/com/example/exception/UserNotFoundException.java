package com.example.exception;

import static com.example.message.ErrorMessage.USER_NOT_FOUND;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(USER_NOT_FOUND.formatWith(id));
    }

    public UserNotFoundException(String email) {
        super(USER_NOT_FOUND.formatWith(email));
    }
}
