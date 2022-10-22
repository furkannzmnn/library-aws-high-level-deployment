package com.example.lib;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@ContextConfiguration(initializers = AbstractGenericContainer.Initializer.class)
public abstract class AbstractGenericContainer {
    public static final String LOCALSTACK_HOSTNAME = "localhost";
    public static final String LOCALSTACK_REGION = "eu-west-3";
    public static final String LOCALSTACK_ACCESS_KEY = "test";
    public static final String LOCALSTACK_SECRET_KEY = "test";

    // aws config
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            String localStackHost;
            try {
                localStackHost = initializeLocalStack();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,localStackHost);
        }


        private static String initializeLocalStack() throws IOException, InterruptedException {
            localStackContainer.start();
            final Integer mappedPort = localStackContainer.getMappedPort(4566);

            localStackContainer.execInContainer("aws", "--endpoint-url=http://0.0.0.0:" + 4566, "secretsmanager", "create-secret", "--name", "aws/secret", "--secret-string", "{\"accessKey\":\"" + LOCALSTACK_ACCESS_KEY + "\",\"secretKey\":\"" + LOCALSTACK_SECRET_KEY + "\"}");
             return "cloud.aws.secrets-manager.end-point.uri=http://localhost:" + mappedPort;
        }

        static LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.17"))
                .withServices(LocalStackContainer.Service.SECRETSMANAGER)
                .withExposedPorts(4566, 4566)
                .waitingFor(Wait.forLogMessage("Ready.*", 1))
                .withEnv("HOSTNAME_EXTERNAL", LOCALSTACK_HOSTNAME)
                .withEnv("DEFAULT_REGION", LOCALSTACK_REGION)
                .withEnv("AWS_ACCESS_KEY_ID", LOCALSTACK_ACCESS_KEY)
                .withEnv("AWS_SECRET_ACCESS_KEY", LOCALSTACK_SECRET_KEY)
                .withEnv("PLATFORM", "linux/x86_64");


    }
}
