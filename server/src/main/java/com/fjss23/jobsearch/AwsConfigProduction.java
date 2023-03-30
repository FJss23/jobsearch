package com.fjss23.jobsearch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@Profile("prod")
public class AwsConfigProduction {
    private static final Region DEFAULT_REGION = Region.EU_WEST_3;

    @Bean
    public SesClient sesClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("foo", "bar");

        return SesClient.builder()
            .region(DEFAULT_REGION)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
    }

    @Bean
    public SnsClient snsClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("foo", "bar");

        return SnsClient.builder()
            .region(DEFAULT_REGION)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
    }

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("foo", "bar");

        return S3Client.builder()
            .region(DEFAULT_REGION)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
    }
}
