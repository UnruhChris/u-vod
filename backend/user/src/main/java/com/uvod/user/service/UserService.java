package com.uvod.user.service;

import com.uvod.user.dto.FavoriteRequest;
import com.uvod.user.dto.RegisterRequest;
import com.uvod.user.dto.UpdateProfileRequest;
import com.uvod.user.dto.UserResponse;
import com.uvod.user.exception.FavoriteLimitReachedException;
import com.uvod.user.exception.UserAlreadyExistsException;
import com.uvod.user.exception.UserNotFoundException;
import com.uvod.user.mapper.UserMapper;
import com.uvod.user.model.FavoriteItem;
import com.uvod.user.model.SupportedLocale;
import com.uvod.user.model.User;
import com.uvod.user.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Service for user management.
 * Contains the business logic separated from the controller.
 */
@Service
public class UserService {

    private static final int MAX_FAVORITES = 30;

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserService(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves a user's profile.
     *
     * @param userId User ID (from X-User-Id header)
     * @return UserResponse with profile data
     * @throws UserNotFoundException if the user does not exist
     */
    public UserResponse getProfile(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userMapper.toResponse(user);
    }

    /**
     * Checks if a user is already registered.
     *
     * @param userId User ID (from X-User-Id header)
     * @return true if the user exists, false otherwise
     */
    public boolean isRegistered(String userId) {
        return userRepo.existsById(userId);
    }

    /**
     * Registers a new user.
     *
     * @param userId   User ID (from X-User-Id header)
     * @param provider Identity provider (from X-User-Provider header)
     * @param request  Registration data (username, email, avatarUrl, locale)
     * @return UserResponse with the created user data
     * @throws UserAlreadyExistsException if the user is already registered
     */
    public UserResponse register(String userId, String provider, RegisterRequest request) {

        if (userRepo.existsById(userId)) {
            throw new UserAlreadyExistsException(userId);
        }

        Instant now = Instant.now();

        User user = User.builder()
                .id(userId)
                .username(request.getUsername())
                .email(request.getEmail())
                .identityProvider(provider)
                .avatarUrl(request.getAvatarUrl())
                .locale(request.getLocale() != null ? request.getLocale() : SupportedLocale.IT)
                .createdAt(now)
                .updatedAt(now)
                .favorites(new ArrayList<>())
                .build();

        User savedUser = userRepo.save(user);

        return userMapper.toResponse(savedUser);
    }

    /**
     * Updates the user's profile (username, avatarUrl, locale).
     *
     * @param userId  User ID (from X-User-Id header)
     * @param request Update data
     * @return UserResponse with updated profile data
     * @throws UserNotFoundException if the user does not exist
     */
    public UserResponse updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setUsername(request.getUsername());
        user.setAvatarUrl(request.getAvatarUrl());
        if (request.getLocale() != null) {
            user.setLocale(request.getLocale());
        }
        user.setUpdatedAt(Instant.now());

        User savedUser = userRepo.save(user);
        return userMapper.toResponse(savedUser);
    }

    /**
     * Adds a film to the user's favorites list.
     * Idempotent: if the film is already present, returns 200 with current data.
     *
     * @param userId  User ID (from X-User-Id header)
     * @param request Favorite data (denormalized film info)
     * @return UserResponse with updated favorites
     * @throws UserNotFoundException         if the user does not exist
     * @throws FavoriteLimitReachedException if the list is at max capacity (30)
     */
    public UserResponse addFavorite(String userId, FavoriteRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Idempotent: if already present, return current state
        boolean alreadyPresent = user.getFavorites().stream()
                .anyMatch(f -> f.getMovieId().equals(request.getMovieId()));

        if (alreadyPresent) {
            return userMapper.toResponse(user);
        }

        // Check limit
        if (user.getFavorites().size() >= MAX_FAVORITES) {
            throw new FavoriteLimitReachedException();
        }

        FavoriteItem item = userMapper.toFavoriteItem(request);
        user.getFavorites().add(item);
        user.setUpdatedAt(Instant.now());

        User savedUser = userRepo.save(user);
        return userMapper.toResponse(savedUser);
    }

    /**
     * Removes a film from the user's favorites list.
     * Idempotent: if the film is not present, returns 200 with current data.
     *
     * @param userId  User ID (from X-User-Id header)
     * @param movieId The film ID to remove
     * @return UserResponse with updated favorites
     * @throws UserNotFoundException if the user does not exist
     */
    public UserResponse removeFavorite(String userId, String movieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        boolean removed = user.getFavorites().removeIf(f -> f.getMovieId().equals(movieId));

        if (removed) {
            user.setUpdatedAt(Instant.now());
            User savedUser = userRepo.save(user);
            return userMapper.toResponse(savedUser);
        }

        return userMapper.toResponse(user);
    }
}