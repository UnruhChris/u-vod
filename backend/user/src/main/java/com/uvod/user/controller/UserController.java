package com.uvod.user.controller;

import com.uvod.user.dto.FavoriteRequest;
import com.uvod.user.dto.RegisterRequest;
import com.uvod.user.dto.UpdateProfileRequest;
import com.uvod.user.dto.UserResponse;
import com.uvod.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for user management.
 *
 * Authentication is handled by the Gateway (Gateway Offloading Pattern):
 * the gateway decodes x-ms-client-principal and injects clean headers
 * X-User-Id, X-User-Name, X-User-Provider.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /user/profile
     * Returns the user profile if it exists, otherwise 404.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(
            @RequestHeader("X-User-Id") String userId) {

        UserResponse response = userService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /user/is-registered
     * Checks if the user is already registered (useful for the frontend).
     */
    @GetMapping("/is-registered")
    public ResponseEntity<Map<String, Boolean>> isRegistered(
            @RequestHeader("X-User-Id") String userId) {

        boolean registered = userService.isRegistered(userId);
        return ResponseEntity.ok(Map.of("registered", registered));
    }

    /**
     * POST /user/register
     * Registers a new user. Returns 409 if already exists.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Provider") String provider,
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(userId, provider, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /user/profile
     * Updates the user's profile (username, avatarUrl, locale).
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody UpdateProfileRequest request) {

        UserResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /user/favorites
     * Adds a film to the user's favorites. Idempotent: returns 200 if already
     * present.
     */
    @PostMapping("/favorites")
    public ResponseEntity<UserResponse> addFavorite(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody FavoriteRequest request) {

        UserResponse response = userService.addFavorite(userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /user/favorites/{movieId}
     * Removes a film from the user's favorites. Idempotent.
     */
    @DeleteMapping("/favorites/{movieId}")
    public ResponseEntity<UserResponse> removeFavorite(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String movieId) {

        UserResponse response = userService.removeFavorite(userId, movieId);
        return ResponseEntity.ok(response);
    }
}