package com.example.lib.api;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;

@ContextConfiguration(initializers = AbstractGenericContainer.Initializer.class)
public class AbstractGenericContainer {
    public static final String LOCALSTACK_HOSTNAME = "localhost";
    public static final String LOCALSTACK_REGION = "eu-west-3";
    public static final String LOCALSTACK_ACCESS_KEY = "test";
    public static final String LOCALSTACK_SECRET_KEY = "test";

    // aws config
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
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
            localStackContainer.waitingFor(Wait.forHttp("/health").forStatusCode(200));
            localStackContainer.execInContainer("aws --endpoint-url=http://localhost:4566 secretsmanager create-secret --name aws/secret --secret-string '{\"my_uname\":\"username\",\"my_pwd\":\"password\"}'");
            final Integer mappedPort = localStackContainer.getMappedPort(4566);
            return "cloud.aws.secrets-manager.end-point.uri=http://localhost:" + mappedPort;
        }

        static LocalStackContainer localStackContainer = new LocalStackContainer()
                .withServices(LocalStackContainer.Service.S3)
                .withServices(LocalStackContainer.Service.SECRETSMANAGER)
                .withExposedPorts(4566, 4566)
                .waitingFor(Wait.forLogMessage(".*Ready.*", 1))
                .withEnv("HOSTNAME_EXTERNAL", LOCALSTACK_HOSTNAME)
                .withEnv("DEFAULT_REGION", LOCALSTACK_REGION)
                .withEnv("AWS_ACCESS_KEY_ID", LOCALSTACK_ACCESS_KEY)
                .withEnv("AWS_SECRET_ACCESS_KEY", LOCALSTACK_SECRET_KEY);


    }
}
