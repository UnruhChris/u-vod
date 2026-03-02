package com.uvod.catalog.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.GatewayConnectionConfig;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableCosmosRepositories(basePackages = "com.uvod.catalog.repository")
public class CatalogCosmosConfig extends AbstractCosmosConfiguration {

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
            // Production: use Managed Identity
            return new CosmosClientBuilder()
                    .endpoint(uri)
                    .credential(new DefaultAzureCredentialBuilder().build());
        }
        // Local development: use key + gateway mode for the emulator
        GatewayConnectionConfig gatewayConfig = new GatewayConnectionConfig();

        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key)
                .gatewayMode(gatewayConfig)
                .endpointDiscoveryEnabled(false);
    }
}
