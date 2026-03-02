package com.uvod.common.exception;

import com.uvod.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler for exceptions common to all microservices.
 * Intercepts exceptions and returns uniform responses.
 * 
 * Priority order: microservice-specific handlers take
 * precedence over these generic handlers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles missing authentication header → 401 Unauthorized
     */
    @ExceptionHandler(MissingPrincipalException.class)
    public ResponseEntity<ErrorResponse> handleMissingPrincipal(
            MissingPrincipalException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    /**
     * Handles invalid authentication header → 401 Unauthorized
     */
    @ExceptionHandler(InvalidPrincipalException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPrincipal(
            InvalidPrincipalException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    /**
     * Handles Bean Validation errors (@Valid) → 400 Bad Request
     * Extracts all error messages from invalid fields.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Collects all validation errors into a single message
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Fallback for unhandled exceptions → 500 Internal Server Error
     * In production, avoid exposing internal details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {
        // Log the error (in a real app you would use a logger)
        ex.printStackTrace();

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                request);
    }

    /**
     * Helper to build the error response.
     */
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(error);
    }
}