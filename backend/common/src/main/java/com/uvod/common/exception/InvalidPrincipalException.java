package com.uvod.common.exception;

/**
 * Thrown when the x-ms-client-principal header is invalid
 * (not Base64, malformed JSON, missing userId, etc.)
 */
public class InvalidPrincipalException extends RuntimeException {
    
    public InvalidPrincipalException(String message) {
        super(message);
    }

    public InvalidPrincipalException(String message, Throwable cause) {
        super(message, cause);
    }
}