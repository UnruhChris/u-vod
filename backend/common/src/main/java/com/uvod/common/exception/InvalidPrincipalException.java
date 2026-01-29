package com.uvod.common.exception;

/**
 * Lanciata quando l'header x-ms-client-principal non è valido
 * (non è Base64, JSON malformato, userId mancante, ecc.)
 */
public class InvalidPrincipalException extends RuntimeException {
    
    public InvalidPrincipalException(String message) {
        super(message);
    }

    public InvalidPrincipalException(String message, Throwable cause) {
        super(message, cause);
    }
}