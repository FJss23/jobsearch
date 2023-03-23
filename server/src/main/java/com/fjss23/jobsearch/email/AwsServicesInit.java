package com.fjss23.jobsearch.email;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.NotificationType;
import software.amazon.awssdk.services.ses.model.SetIdentityNotificationTopicRequest;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Component
public class AwsServicesInit {
    private final SnsClient snsClient;
    private final SesClient sesClient;
    private final S3Client s3Client;

    public AwsServicesInit(SnsClient snsClient, SesClient sesClient, S3Client s3Client) {
        this.snsClient = snsClient;
        this.sesClient = sesClient;
        this.s3Client = s3Client;
    }
}
