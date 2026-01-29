package com.uvod.user.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.GatewayConnectionConfig;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCosmosRepositories(basePackages = "com.uvod.user.repository")
public class UserCosmosConfig extends AbstractCosmosConfiguration {

    @Value("${AZURE_COSMOS_URI}")
    private String uri;

    @Value("${AZURE_COSMOS_KEY}")
    private String key;

    @Value("${AZURE_COSMOS_DB}")
    private String dbName;

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        GatewayConnectionConfig gatewayConfig = new GatewayConnectionConfig();

        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key)
                .gatewayMode(gatewayConfig)
                .endpointDiscoveryEnabled(false);
    }
}