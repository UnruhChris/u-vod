package com.uvod.user.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.GatewayConnectionConfig;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

import ch.qos.logback.core.util.StringUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableCosmosRepositories(basePackages = "com.uvod.user.repository")
public class UserCosmosConfig extends AbstractCosmosConfiguration {

    @Value("${AZURE_COSMOS_URI}")
    private String uri;

    @Value("${AZURE_COSMOS_KEY:}")
    private String key;

    @Value("${AZURE_COSMOS_DB}")
    private String dbName;

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        if (!StringUtils.hasText(key)) {
            // Use Managed Identity
            return new CosmosClientBuilder()
                    .endpoint(uri)
                    .credential(new DefaultAzureCredentialBuilder().build());
        }
        GatewayConnectionConfig gatewayConfig = new GatewayConnectionConfig();

        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key)
                .gatewayMode(gatewayConfig)
                .endpointDiscoveryEnabled(false);
    }
}