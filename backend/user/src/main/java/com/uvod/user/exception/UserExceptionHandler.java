package com.uvod.user.exception;

import com.uvod.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler for User domain-specific exceptions.
 * 
 * @Order(Ordered.HIGHEST_PRECEDENCE) ensures that this handler
 *                                    is evaluated BEFORE the
 *                                    GlobalExceptionHandler from common.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    /**
     * Handles UserNotFoundException → 404 Not Found
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles UserAlreadyExistsException → 409 Conflict
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles FavoriteLimitReachedException → 400 Bad Request
     */
    @ExceptionHandler(FavoriteLimitReachedException.class)
    public ResponseEntity<ErrorResponse> handleFavoriteLimitReached(
            FavoriteLimitReachedException ex,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Helper to build the error response.
     * Uses the same ErrorResponse format from the common module.
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
