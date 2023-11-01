package com.example.exception.handler;

import com.example.dto.ErrorDto;
import com.example.exception.MessageNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(createErrorDto(e), NOT_FOUND);
    }

    private ErrorDto createErrorDto(Exception e) {
        return ErrorDto.of(e.getMessage());
    }
}
