package com.uvod.common.exception;

/**
 * Lanciata quando l'header x-ms-client-principal è assente.
 * Indica che l'utente non è autenticato.
 */
public class MissingPrincipalException extends RuntimeException {
    
    public MissingPrincipalException(String message) {
        super(message);
    }
}