package com.example.exception.handler;

import com.example.dto.ErrorDto;
import com.example.exception.UserNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errorsMap = getErrorsMapFrom(exception);
        return new ResponseEntity<>(errorsMap, BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(UserNotFoundException ex) {
        return createErrorDto(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleServerException(Exception ex) {
        return createErrorDto(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDto> createErrorDto(Exception ex, HttpStatus httpStatus) {
        ErrorDto errorDto = ErrorDto.of(ex.getMessage());
        return new ResponseEntity<>(errorDto, httpStatus);
    }

    private Map<String, String> getErrorsMapFrom(MethodArgumentNotValidException exception) {
        List<ObjectError> objectErrors = getObjectErrorsFrom(exception);
        return mapObjectErrorsWithMessages(objectErrors);
    }

    private List<ObjectError> getObjectErrorsFrom(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return bindingResult.getAllErrors();
    }

    @SuppressWarnings("DataFlowIssue")
    private Map<String, String> mapObjectErrorsWithMessages(List<ObjectError> objectErrors) {
        return objectErrors.stream()
                .map(objectError -> (FieldError) objectError)
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
    }
}
