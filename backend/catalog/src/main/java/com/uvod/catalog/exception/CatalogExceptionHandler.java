package com.uvod.catalog.exception;

import com.uvod.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler for Catalog domain-specific exceptions.
 *
 * @Order(Ordered.HIGHEST_PRECEDENCE) ensures that this handler
 *                                    is evaluated first
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CatalogExceptionHandler {

    /**
     * Handles FilmNotFoundException → 404 Not Found
     */
    @ExceptionHandler(FilmNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFilmNotFound(
            FilmNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
