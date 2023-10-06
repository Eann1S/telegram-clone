package com.example.exception;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class MissingAuthorizationHeaderException extends ResponseStatusException {

    public MissingAuthorizationHeaderException() {
        super(FORBIDDEN, "You haven't access to this resource. Please sign in and try again.");
    }
}
