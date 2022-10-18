package com.example.lib;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

@Profile("integration")
@TestConfiguration
public class LocalStackConfig {

    // Any mock-AWS services required for any tests need to be added to this
    // manifest so that LocalStack knows what to spin up.
    private static final LocalStackContainer.Service[] REQUIRED_SERVICES = {
            LocalStackContainer.Service.S3,
            LocalStackContainer.Service.SECRETSMANAGER,
    };

    private LocalStackContainer localStackContainer;

    @Bean
    public LocalStackContainer localStackContainer() {
        localStackContainer = new LocalStackContainer()
                .withServices(REQUIRED_SERVICES)
                .withEnv("HOSTNAME_EXTERNAL", "localhost");
        localStackContainer.start();

        return this.localStackContainer;
    }
}
