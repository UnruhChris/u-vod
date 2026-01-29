package com.uvod.common.config;

import com.uvod.common.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configurazione per il modulo common.
 * Registra automaticamente i componenti condivisi quando
 * un microservizio importa questa libreria come dipendenza.
 */
@AutoConfiguration
public class CommonAutoConfiguration {

    /**
     * Registra il GlobalExceptionHandler come bean.
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}