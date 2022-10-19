package com.example.lib.configure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AwsConfigure {
    public static final String REGION = "eu-west-3";
    private final Map<String, String> secretCache = new LinkedHashMap<>();

    @Value("${cloud.aws.end-point.uri}")
    private String s3Url;

    @Value("${cloud.aws.secrets-manager.end-point.uri}")
    private String secretManagerUrl;

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() throws JsonProcessingException {
        String secretName = "aws/secret";
        String region = "eu-west-3";

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withEndpointConfiguration(new EndpointConfiguration(secretManagerUrl, region))
                .build();

        String secret;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult;

        getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        secret = getSecretValueResult.getSecretString();

        ObjectMapper m = new ObjectMapper();
        Map<String, String>  read = m.readValue(secret, Map.class);
        read.forEach((key, value) -> {
            secretCache.put("accessKey", key);
            secretCache.put("secretKey", value);
        });
    }

    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(secretCache.get("accessKey"), secretCache.get("secretKey"))))
                .withEndpointConfiguration(getEndpointConfiguration(s3Url))
                .build();
    }

    private EndpointConfiguration getEndpointConfiguration(String url) {
        return new EndpointConfiguration(url, REGION);
    }


}
