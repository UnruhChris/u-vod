package com.uvod.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO che rappresenta i dati dell'utente autenticato estratti
 * dall'header x-ms-client-principal di Azure Static Web Apps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientPrincipal {

    /** ID univoco dell'utente (generato da SWA) */
    private String userId;

    /** Username o email dell'utente (dal provider) */
    private String userDetails;

    /** Provider di autenticazione (es. "github", "aad") */
    private String identityProvider;

    /** Ruoli assegnati all'utente */
    private List<String> userRoles;
}