package com.uvod.user.controller;

import com.uvod.common.dto.ClientPrincipal;
import com.uvod.common.security.PrincipalParser;
import com.uvod.user.dto.RegisterRequest;
import com.uvod.user.dto.UserResponse;
import com.uvod.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /user/profile
     * Restituisce il profilo utente se esiste, altrimenti 404.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(
            @RequestHeader(value = "x-ms-client-principal", required = false) String principalHeader) {

        ClientPrincipal principal = PrincipalParser.parse(principalHeader);
        UserResponse response = userService.getProfile(principal.getUserId());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /user/is-registered
     * Verifica se l'utente è già registrato (utile per il frontend).
     */
    @GetMapping("/is-registered")
    public ResponseEntity<Map<String, Boolean>> isRegistered(
            @RequestHeader(value = "x-ms-client-principal", required = false) String principalHeader) {

        ClientPrincipal principal = PrincipalParser.parse(principalHeader);
        boolean registered = userService.isRegistered(principal.getUserId());

        return ResponseEntity.ok(Map.of("registered", registered));
    }

    /**
     * POST /user/register
     * Registra un nuovo utente. Restituisce 409 se già esiste.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestHeader(value = "x-ms-client-principal", required = false) String principalHeader,
            @Valid @RequestBody RegisterRequest request) {

        ClientPrincipal principal = PrincipalParser.parse(principalHeader);
        UserResponse response = userService.register(principal, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}