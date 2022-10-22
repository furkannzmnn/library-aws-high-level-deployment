package com.example.lib.api;

import com.example.lib.AbstractGenericContainer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "integration")
public abstract class BaseRestControllerTest extends AbstractGenericContainer {

}
