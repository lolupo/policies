package com.tinubu.policies.application.api;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final String PROBLEM_JSON = "application/problem+json";

    /**
     * Handles validation errors for DTOs annotated with @Valid.
     * Returns RFC 7807 application/problem+json error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ApiErrorResponse error = new ApiErrorResponse(
                "https://example.com/problems/validation-error",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                errorMsg,
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.valueOf(PROBLEM_JSON))
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                "https://example.com/problems/bad-request",
                "Bad Request",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.valueOf(PROBLEM_JSON))
                .body(error);
    }

    private String extractDetail(HttpMessageNotReadableException ex) {
        // Try to extract a more specific message for enum mapping errors
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            String fieldName = ife.getPathReference();
            String invalidValue = String.valueOf(ife.getValue());
            String targetType = ife.getTargetType().getSimpleName();
            return String.format("Invalid value '%s' for field %s. Expected type: %s.", invalidValue, fieldName, targetType);
        }
        return "Malformed JSON request or invalid value. " + ex.getMessage();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String detail = extractDetail(ex);
        ApiErrorResponse error = new ApiErrorResponse(
                "https://example.com/problems/invalid-json",
                "Invalid JSON or value",
                HttpStatus.BAD_REQUEST.value(),
                detail,
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.valueOf(PROBLEM_JSON))
                .body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                "https://example.com/problems/internal-error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.valueOf(PROBLEM_JSON))
                .body(error);
    }
}
