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
 * Utility per decodificare l'header x-ms-client-principal di Azure Static Web
 * Apps.
 * 
 * L'header contiene un JSON codificato in Base64 con questa struttura:
 * {
 * "userId": "abc123",
 * "userDetails": "user@example.com",
 * "identityProvider": "github",
 * "userRoles": ["authenticated", "anonymous"]
 * }
 */
public final class PrincipalParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Costruttore privato: utility class, non istanziabile
    private PrincipalParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Decodifica l'header x-ms-client-principal.
     *
     * @param principalHeader Header Base64-encoded (può essere null)
     * @return ClientPrincipal con i dati dell'utente
     * @throws MissingPrincipalException se l'header è null o vuoto
     * @throws InvalidPrincipalException se l'header non è valido
     */
    public static ClientPrincipal parse(String principalHeader) {
        // 1. Verifica presenza header
        if (principalHeader == null || principalHeader.isBlank()) {
            throw new MissingPrincipalException("Header x-ms-client-principal mancante");
        }

        try {
            // 2. Decodifica Base64
            byte[] decodedBytes = Base64.getDecoder().decode(principalHeader);
            String json = new String(decodedBytes, StandardCharsets.UTF_8);

            // 3. Parse JSON
            @SuppressWarnings("unchecked")
            Map<String, Object> map = OBJECT_MAPPER.readValue(json, Map.class);

            // 4. Estrai userId (obbligatorio)
            String userId = (String) map.get("userId");
            if (userId == null || userId.isBlank()) {
                throw new InvalidPrincipalException("userId mancante nel principal");
            }

            // 5. Estrai altri campi (opzionali)
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) map.getOrDefault("userRoles", List.of());

            // 6. Costruisci e restituisci DTO
            return ClientPrincipal.builder()
                    .userId(userId)
                    .userDetails((String) map.get("userDetails"))
                    .identityProvider((String) map.get("identityProvider"))
                    .userRoles(roles)
                    .build();

        } catch (IllegalArgumentException e) {
            // Base64.getDecoder().decode() fallito
            throw new InvalidPrincipalException("Header non è un Base64 valido", e);
        } catch (InvalidPrincipalException e) {
            // Ri-lancia le nostre eccezioni
            throw e;
        } catch (Exception e) {
            // Qualsiasi altro errore (JSON malformato, ecc.)
            throw new InvalidPrincipalException("Errore nel parsing del principal: " + e.getMessage(), e);
        }
    }
}