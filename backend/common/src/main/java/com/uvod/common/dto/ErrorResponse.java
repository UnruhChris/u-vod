package com.uvod.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Standard DTO for API error responses.
 * Provides a uniform structure for all errors.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /** HTTP status code (e.g. 404, 500) */
    private int status;

    /** HTTP error name (e.g. "Not Found", "Internal Server Error") */
    private String error;

    /** Descriptive error message */
    private String message;

    /** Request path that generated the error */
    private String path;

    /** Error timestamp in ISO-8601 format */
    @Builder.Default
    private String timestamp = Instant.now().toString();
}