package com.uvod.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uvod.common.exception.InvalidPrincipalException;
import com.uvod.common.exception.MissingPrincipalException;
import com.uvod.common.dto.ClientPrincipal;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Utility to decode the x-ms-client-principal header from Azure Static Web
 * Apps.
 * 
 * The header contains a Base64-encoded JSON with this structure:
 * {
 * "userId": "abc123",
 * "userDetails": "user@example.com",
 * "identityProvider": "github",
 * "userRoles": ["authenticated", "anonymous"]
 * }
 */
public final class PrincipalParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Private constructor: utility class, not instantiable
    private PrincipalParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Decodes the x-ms-client-principal header.
     *
     * @param principalHeader Base64-encoded header (can be null)
     * @return ClientPrincipal with the user data
     * @throws MissingPrincipalException if the header is null or empty
     * @throws InvalidPrincipalException if the header is invalid
     */
    public static ClientPrincipal parse(String principalHeader) {
        // 1. Check header presence
        if (principalHeader == null || principalHeader.isBlank()) {
            throw new MissingPrincipalException("Missing x-ms-client-principal header");
        }

        try {
            // 2. Decode Base64
            byte[] decodedBytes = Base64.getDecoder().decode(principalHeader);
            String json = new String(decodedBytes, StandardCharsets.UTF_8);

            // 3. Parse JSON
            @SuppressWarnings("unchecked")
            Map<String, Object> map = OBJECT_MAPPER.readValue(json, Map.class);

            // 4. Extract userId (required)
            String userId = (String) map.get("userId");
            if (userId == null || userId.isBlank()) {
                throw new InvalidPrincipalException("userId missing in principal");
            }

            // 5. Extract other fields (optional)
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) map.getOrDefault("userRoles", List.of());

            // 6. Build and return DTO
            return ClientPrincipal.builder()
                    .userId(userId)
                    .userDetails((String) map.get("userDetails"))
                    .identityProvider((String) map.get("identityProvider"))
                    .userRoles(roles)
                    .build();

        } catch (IllegalArgumentException e) {
            // Base64.getDecoder().decode() failed
            throw new InvalidPrincipalException("Header is not valid Base64", e);
        } catch (InvalidPrincipalException e) {
            // Re-throw our exceptions
            throw e;
        } catch (Exception e) {
            // Any other error (malformed JSON, etc.)
            throw new InvalidPrincipalException("Error parsing principal: " + e.getMessage(), e);
        }
    }
}