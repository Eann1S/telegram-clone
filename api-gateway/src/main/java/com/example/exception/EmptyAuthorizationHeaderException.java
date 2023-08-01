package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmptyAuthorizationHeaderException extends ResponseStatusException {

    public EmptyAuthorizationHeaderException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
