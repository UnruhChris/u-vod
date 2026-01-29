package com.uvod.user.exception;

/**
 * Eccezione lanciata quando si tenta di creare un utente già esistente.
 * Viene convertita in HTTP 409 Conflict da UserExceptionHandler.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String userId) {
        super("Utente già registrato con ID: " + userId);
    }

    public UserAlreadyExistsException(String message, boolean customMessage) {
        super(message);
    }
}
