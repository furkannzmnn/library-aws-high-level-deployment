package com.example.lib.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "integration")
public abstract class BaseServiceTest {

    @Autowired
    private static LocalStackContainer localStackContainer;

    @DynamicPropertySource
    static void setCredentials(DynamicPropertyRegistry registry) {
        var credentials = localStackContainer.getDefaultCredentialsProvider().getCredentials();
        registry.add("cloud.aws.region.static", () -> "eu-west-3");
        registry.add("cloud.aws.credentials.access-key", credentials::getAWSAccessKeyId);
        registry.add("cloud.aws.credentials.secret-key", credentials::getAWSSecretKey);
        registry.add("cloud.aws.end-point.uri", () -> localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3));
    }
}
