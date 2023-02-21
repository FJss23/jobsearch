package com.fjss23.jobsearch.email;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * Examples:
 * - https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html
 */
@Configuration
public class AwsConfig {

    private static final String ENDPOINT_URL = "http://localhost:4566";
    private static final Region DEFAULT_REGION = Region.EU_WEST_3;

    @Bean
    public SesClient sesClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
            "foo",
            "bar");

        return SesClient
            .builder()
            .region(DEFAULT_REGION)
            .credentialsProvider(
                StaticCredentialsProvider.create(awsCreds)
            )
            .endpointOverride(URI.create(ENDPOINT_URL))
            .build();
    }
}
