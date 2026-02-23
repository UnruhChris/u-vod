package com.uvod.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SwaAuthFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SwaAuthFilter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Retrieve the auth header injected by SWA
        String clientPrincipalBase64 = exchange.getRequest().getHeaders().getFirst("x-ms-client-principal");

        if (clientPrincipalBase64 != null) {
            try {
                // Decode Base64 string
                String jsonPrincipal = new String(Base64.getDecoder().decode(clientPrincipalBase64),
                        StandardCharsets.UTF_8);

                // Extract user info from JSON
                JsonNode rootNode = objectMapper.readTree(jsonPrincipal);
                String userId = rootNode.path("userId").asText();
                String userDetails = rootNode.path("userDetails").asText();

                logger.info("Authenticated user: {} ({})", userDetails, userId);

                // Add custom headers for downstream microservices
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Name", userDetails)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                logger.error("Error decoding SWA header", e);
            }
        } else {
            logger.warn("No auth header found (Anonymous user)");
        }

        // Proceed with the original request if auth fails or is missing
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}