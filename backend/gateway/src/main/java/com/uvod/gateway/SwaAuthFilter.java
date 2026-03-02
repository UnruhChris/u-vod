package com.uvod.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gateway Offloading Filter.
 *
 * Decodes the x-ms-client-principal header injected by SWA,
 * blocks anonymous requests (401), and propagates to downstream
 * microservices only clean headers: X-User-Id, X-User-Name, X-User-Provider.
 */
@Component
public class SwaAuthFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SwaAuthFilter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientPrincipalBase64 = exchange.getRequest().getHeaders().getFirst("x-ms-client-principal");

        // Anonymous request → 401 Unauthorized
        if (clientPrincipalBase64 == null || clientPrincipalBase64.isBlank()) {
            logger.warn("Request blocked: no x-ms-client-principal header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            // Decode Base64 → JSON
            String jsonPrincipal = new String(
                    Base64.getDecoder().decode(clientPrincipalBase64),
                    StandardCharsets.UTF_8);

            JsonNode rootNode = objectMapper.readTree(jsonPrincipal);
            String userId = rootNode.path("userId").asText();
            String userDetails = rootNode.path("userDetails").asText();
            String identityProvider = rootNode.path("identityProvider").asText();

            if (userId == null || userId.isBlank()) {
                logger.warn("Request blocked: userId missing in principal");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            logger.info("Authenticated user: {} ({}) via {}", userDetails, userId, identityProvider);

            // Propagate clean headers and strip the original
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .headers(h -> h.remove("x-ms-client-principal"))
                    .header("X-User-Id", userId)
                    .header("X-User-Name", userDetails)
                    .header("X-User-Provider", identityProvider)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            logger.error("Error decoding SWA header", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}