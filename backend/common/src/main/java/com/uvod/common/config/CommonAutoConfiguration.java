package com.uvod.common.config;

import com.uvod.common.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for the common module.
 * Automatically registers shared components when
 * a microservice imports this library as a dependency.
 */
@AutoConfiguration
public class CommonAutoConfiguration {

    /**
     * Registers the GlobalExceptionHandler as a bean.
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}