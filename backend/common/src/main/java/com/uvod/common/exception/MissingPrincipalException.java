package com.uvod.common.exception;

/**
 * Thrown when the x-ms-client-principal header is missing.
 * Indicates that the user is not authenticated.
 */
public class MissingPrincipalException extends RuntimeException {
    
    public MissingPrincipalException(String message) {
        super(message);
    }
}