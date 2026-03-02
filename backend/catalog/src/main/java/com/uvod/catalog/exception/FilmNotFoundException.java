package com.uvod.catalog.exception;

/**
 * Exception thrown when a film is not found in the database.
 * Converted to HTTP 404 Not Found by CatalogExceptionHandler.
 */
public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(String filmId) {
        super("Film not found with ID: " + filmId);
    }
}
