package com.uvod.common.exception;

import com.uvod.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Handler globale per le eccezioni comuni a tutti i microservizi.
 * Intercetta le eccezioni e restituisce risposte uniformi.
 * 
 * Ordine di priorità: gli handler specifici dei microservizi hanno
 * precedenza su questi handler generici.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce header di autenticazione mancante → 401 Unauthorized
     */
    @ExceptionHandler(MissingPrincipalException.class)
    public ResponseEntity<ErrorResponse> handleMissingPrincipal(
            MissingPrincipalException ex, 
            HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    /**
     * Gestisce header di autenticazione non valido → 401 Unauthorized
     */
    @ExceptionHandler(InvalidPrincipalException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPrincipal(
            InvalidPrincipalException ex, 
            HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    /**
     * Gestisce errori di validazione Bean Validation (@Valid) → 400 Bad Request
     * Estrae tutti i messaggi di errore dai campi non validi.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        // Raccoglie tutti gli errori di validazione in un unico messaggio
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Fallback per eccezioni non gestite → 500 Internal Server Error
     * In produzione, evita di esporre dettagli interni.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, 
            HttpServletRequest request) {
        // Log dell'errore (in un'app reale useresti un logger)
        ex.printStackTrace();
        
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Errore interno del server", 
                request);
    }

    /**
     * Helper per costruire la risposta di errore.
     */
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, 
            String message, 
            HttpServletRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(status).body(error);
    }
}