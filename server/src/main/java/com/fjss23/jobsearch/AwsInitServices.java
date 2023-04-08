package com.fjss23.jobsearch;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;

@Component
public class AwsInitServices {
    private final SnsClient snsClient;
    private final SesClient sesClient;
    private final S3Client s3Client;

    public AwsInitServices(SnsClient snsClient, SesClient sesClient, S3Client s3Client) {
        this.snsClient = snsClient;
        this.sesClient = sesClient;
        this.s3Client = s3Client;
    }
}
