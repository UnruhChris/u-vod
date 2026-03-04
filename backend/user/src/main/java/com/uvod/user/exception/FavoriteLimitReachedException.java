package com.uvod.user.exception;

/**
 * Exception thrown when a user tries to add a favorite
 * but has already reached the maximum limit (30).
 * Converted to HTTP 400 Bad Request by UserExceptionHandler.
 */
public class FavoriteLimitReachedException extends RuntimeException {

    private static final int MAX_FAVORITES = 30;

    public FavoriteLimitReachedException() {
        super("Favorites limit reached. Maximum allowed: " + MAX_FAVORITES);
    }
}
