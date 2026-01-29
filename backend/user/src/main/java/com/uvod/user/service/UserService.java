package com.uvod.user.service;

import com.uvod.common.dto.ClientPrincipal;
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
 * Service per la gestione degli utenti.
 * Contiene la logica di business separata dal controller.
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
     * Recupera il profilo di un utente.
     *
     * @param userId ID dell'utente (da ClientPrincipal)
     * @return UserResponse con i dati del profilo
     * @throws UserNotFoundException se l'utente non esiste
     */
    public UserResponse getProfile(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userMapper.toResponse(user);
    }

    /**
     * Verifica se un utente è già registrato.
     *
     * @param userId ID dell'utente (da ClientPrincipal)
     * @return true se l'utente esiste, false altrimenti
     */
    public boolean isRegistered(String userId) {
        return userRepo.existsById(userId);
    }

    /**
     * Registra un nuovo utente.
     *
     * @param principal Dati dell'utente autenticato da Azure SWA
     * @param request   Dati di registrazione (visibleUsername, email)
     * @return UserResponse con i dati dell'utente creato
     * @throws UserAlreadyExistsException se l'utente è già registrato
     */
    public UserResponse register(ClientPrincipal principal, RegisterRequest request) {
        String userId = principal.getUserId();

        if (userRepo.existsById(userId)) {
            throw new UserAlreadyExistsException(userId);
        }

        User user = User.builder()
                .id(userId)
                .visibleUsername(request.getVisibleUsername())
                .email(request.getEmail())
                .identityProvider(principal.getIdentityProvider())
                .providerUsername(principal.getUserDetails())
                .registrationDate(Instant.now())
                .favorites(new ArrayList<>())
                .watchHistory(new ArrayList<>())
                .build();

        User savedUser = userRepo.save(user);

        return userMapper.toResponse(savedUser);
    }
}