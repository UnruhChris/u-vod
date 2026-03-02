package com.uvod.user.controller;

import com.uvod.user.dto.RegisterRequest;
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
            @RequestHeader("X-User-Name") String userName,
            @RequestHeader("X-User-Provider") String provider,
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(userId, userName, provider, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}