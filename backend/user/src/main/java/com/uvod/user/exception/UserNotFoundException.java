package com.uvod.user.exception;

/**
 * Eccezione lanciata quando un utente non viene trovato nel database.
 * Viene convertita in HTTP 404 Not Found da UserExceptionHandler.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("Utente non trovato con ID: " + userId);
    }

    public UserNotFoundException(String message, boolean customMessage) {
        super(message);
    }
}
