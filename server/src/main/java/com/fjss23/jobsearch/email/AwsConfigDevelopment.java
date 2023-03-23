package com.fjss23.jobsearch.email;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;

/**
 * Examples:
 * - https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html
 */
@Configuration
@Profile("dev") /* Using localstack we need to override the endpoint */
public class AwsConfigDevelopment {

    @Value("${aws.endpoint}")    /* Left this as a property value so testcontainers can use it later */
    private String endpointUrl;
    private static final Region DEFAULT_REGION = Region.EU_WEST_3;

    @Bean
    public SesClient sesClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("foo", "bar");

        return SesClient.builder()
                .region(DEFAULT_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

    @Bean
    public SnsClient snsClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("foo", "bar");

        return SnsClient.builder()
                .region(DEFAULT_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("foo", "bar");

        return S3Client.builder()
                .region(DEFAULT_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }
}
