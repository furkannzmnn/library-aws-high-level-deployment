package com.example.lib;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@ContextConfiguration(initializers = AbstractGenericContainer.Initializer.class)
public abstract class AbstractGenericContainer {
    private static final String LOCALSTACK_REGION = "eu-west-3";
    private static final String LOCALSTACK_ACCESS_KEY = "test";
    private static final String LOCALSTACK_SECRET_KEY = "test";

    private static final int REDIS_PORT = 6379;

    // aws config
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            String localStackHost;
            try {
                localStackHost = initializeLocalStack();
                initializeRedis();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,localStackHost);
        }


        private static String initializeLocalStack() throws IOException, InterruptedException {
            localStackContainer.start();
            final Integer mappedPort = localStackContainer.getMappedPort(4566);

            localStackContainer.execInContainer("aws", "--version");
            localStackContainer.execInContainer("aws", "configure", "set", "aws_access_key_id", LOCALSTACK_ACCESS_KEY);
            localStackContainer.execInContainer("aws", "configure", "set", "aws_secret_access_key", LOCALSTACK_SECRET_KEY);
            localStackContainer.execInContainer("aws", "configure", "set", "region", LOCALSTACK_REGION);
            localStackContainer.execInContainer("aws", "configure", "set", "output", "json");// create secretmanager secret
            localStackContainer.execInContainer("aws", "--endpoint-url=http://0.0.0.0:" + 4566, "secretsmanager", "create-secret", "--name", "aws/secret", "--secret-string", "{\"accessKey\":\"" + LOCALSTACK_ACCESS_KEY + "\",\"secretKey\":\"" + LOCALSTACK_SECRET_KEY + "\"}");
             return "cloud.aws.secrets-manager.end-point.uri=http://localhost:" + mappedPort;
        }

        private static void initializeRedis() throws IOException, InterruptedException {
            redisContainer.start();
            final Integer mappedPort = redisContainer.getMappedPort(REDIS_PORT);
            redisContainer.execInContainer("redis-cli", "-h", "localhost", "-p", String.valueOf(mappedPort), "ping");
        }

        static LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.17"))
                .withServices(LocalStackContainer.Service.SECRETSMANAGER)
                .withExposedPorts(4566, 4566)
                .waitingFor(Wait.forLogMessage("Ready.*", 1))
                .withEnv("HOSTNAME_EXTERNAL", "localstack")
                .withEnv("DEFAULT_REGION", LOCALSTACK_REGION)
                .withEnv("AWS_ACCESS_KEY_ID", LOCALSTACK_ACCESS_KEY)
                .withEnv("AWS_SECRET_ACCESS_KEY", LOCALSTACK_SECRET_KEY)
                .withEnv("PLATFORM", "linux/x86_64");

        static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:6.2.5"))
                .withExposedPorts(REDIS_PORT)
                .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\s", 1));


    }
}
