package com.example.lib.configure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.example.lib.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AwsConfigure {
    public static final String REGION = "eu-west-3";

    public Map<String, String> init() {
        String secretName = "aws/secret";
        String region = "eu-west-3";

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();

        String secret;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        secret = getSecretValueResult.getSecretString();

        secret = secret.replace("{", "");
        secret = secret.replace("}", "");
        secret = secret.replace("\"", "");
        String[] secrets = secret.split(",");
        Map<String, String> map = new LinkedHashMap<>();
        for (String s : secrets) {
            String[] split = s.split(":");
            map.put("accessKey", split[0]);
            map.put("secretKey", split[1]);
        }
        return map;
    }

    @Getter
    @NoArgsConstructor
    @Setter
    private static class Secret {
        private String accessKey;
        private String secretKey;
    }


    @Bean
    public AmazonS3 s3Client() {
        final Map<String, String> secret = init();
        return AmazonS3ClientBuilder.standard()
                .withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(secret.get("accessKey"), secret.get("secretKey"))))
                .build();
    }




}
