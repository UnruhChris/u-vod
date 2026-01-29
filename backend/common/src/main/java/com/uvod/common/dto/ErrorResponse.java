package com.uvod.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO standard per le risposte di errore dell'API.
 * Fornisce una struttura uniforme per tutti gli errori.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /** HTTP status code (es. 404, 500) */
    private int status;
    
    /** Nome dell'errore HTTP (es. "Not Found", "Internal Server Error") */
    private String error;
    
    /** Messaggio descrittivo dell'errore */
    private String message;
    
    /** Path della richiesta che ha generato l'errore */
    private String path;
    
    /** Timestamp dell'errore in formato ISO-8601 */
    @Builder.Default
    private String timestamp = Instant.now().toString();
}