package com.ems.exception;

import com.ems.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors for @Valid annotated request bodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ApiResponse<>("error", "Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    // Handle validation errors for request params, path variables, etc.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ApiResponse<>("error", ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    // Handle illegal arguments (e.g., from service layer)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ApiResponse<>("error", ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    // Handle any uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleOtherExceptions(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>("error", "Internal server error: " + ex.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
