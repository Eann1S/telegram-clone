package com.example.exception.handler;

import com.example.dto.response.ErrorDto;
import com.example.exception.EntityNotFoundException;
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(EntityNotFoundException ex) {
        return generateErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleServerException(Exception ex) {
        return generateErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDto> generateErrorResponse(Exception ex, HttpStatus httpStatus) {
        ErrorDto errorDto = ErrorDto.of(ex.getMessage(), System.currentTimeMillis());
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
