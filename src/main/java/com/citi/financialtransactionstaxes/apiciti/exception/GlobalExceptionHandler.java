package com.citi.financialtransactionstaxes.apiciti.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.citi.financialtransactionstaxes.apiciti.domain.response.GenericResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captures validation errors for @Valid in DTOs and RequestBodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        log.warn("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(new GenericResponse<>("Validation error", errors));
    }

    // Captures when a resource is not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<String>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.warn("Resource not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GenericResponse<>(exception.getMessage(), null));
    }

    // Captures errors when an endpoint is not found (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<GenericResponse<String>> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        log.warn("Invalid path: {}", exception.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GenericResponse<>("Endpoint not found", exception.getRequestURL()));
    }

    // Captures business logic errors
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericResponse<String>> handleBadRequestException(BadRequestException exception) {
        log.warn("Business logic error: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse<>(exception.getMessage(), null));
    }

    // Captures conflict errors (duplicates, rule violations)
    // @ExceptionHandler(ConflictException.class)
    // public ResponseEntity<GenericResponse<String>> handleConflictException(ConflictException exception) {
    //     log.warn("Conflict error: {}", exception.getMessage());
    //     return ResponseEntity.status(HttpStatus.CONFLICT)
    //             .body(new GenericResponse<>(exception.getMessage(), null));
    // }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<GenericResponse<Object>> handleConflictException(ConflictException exception) {
        log.warn("Conflict error: {}", exception.getMessage());

        Object existingData = exception.getData();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GenericResponse<>(exception.getMessage(), existingData));
    }

    // Captures unexpected errors (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<String>> handleException(Exception exception) {
        log.error("Unexpected error: ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericResponse<>("Unexpected error", exception.getMessage()));
    }

}
