package com.uvod.user.service;

import com.uvod.user.dto.RegisterRequest;
import com.uvod.user.dto.UserResponse;
import com.uvod.user.exception.UserAlreadyExistsException;
import com.uvod.user.exception.UserNotFoundException;
import com.uvod.user.mapper.UserMapper;
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
     * @param userName Username from the provider (from X-User-Name header)
     * @param provider Identity provider (from X-User-Provider header)
     * @param request  Registration data (visibleUsername, email)
     * @return UserResponse with the created user data
     * @throws UserAlreadyExistsException if the user is already registered
     */
    public UserResponse register(String userId, String userName, String provider, RegisterRequest request) {

        if (userRepo.existsById(userId)) {
            throw new UserAlreadyExistsException(userId);
        }

        User user = User.builder()
                .id(userId)
                .visibleUsername(request.getVisibleUsername())
                .email(request.getEmail())
                .identityProvider(provider)
                .providerUsername(userName)
                .registrationDate(Instant.now())
                .favorites(new ArrayList<>())
                .watchHistory(new ArrayList<>())
                .build();

        User savedUser = userRepo.save(user);

        return userMapper.toResponse(savedUser);
    }
}