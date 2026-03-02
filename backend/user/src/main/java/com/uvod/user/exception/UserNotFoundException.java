package com.uvod.user.exception;

/**
 * Exception thrown when a user is not found in the database.
 * Converted to HTTP 404 Not Found by UserExceptionHandler.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId);
    }

    public UserNotFoundException(String message, boolean customMessage) {
        super(message);
    }
}
