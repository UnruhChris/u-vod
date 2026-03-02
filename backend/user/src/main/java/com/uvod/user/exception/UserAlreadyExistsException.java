package com.uvod.user.exception;

/**
 * Exception thrown when attempting to create an already existing user.
 * Converted to HTTP 409 Conflict by UserExceptionHandler.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String userId) {
        super("User already registered with ID: " + userId);
    }

    public UserAlreadyExistsException(String message, boolean customMessage) {
        super(message);
    }
}
