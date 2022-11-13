package com.example.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@SpringBootApplication
@EnableCaching
public class LibApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LibApplication.class, args);
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void run(String... args) throws Exception {

        File file = new File("init-aws.sh");
        try {
            Process process = Runtime.getRuntime().exec("sh " + file.getAbsolutePath());
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
