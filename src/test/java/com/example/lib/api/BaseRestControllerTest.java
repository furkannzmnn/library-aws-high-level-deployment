package com.example.lib.api;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "integration")
public abstract class BaseRestControllerTest extends AbstractGenericContainer {

}
