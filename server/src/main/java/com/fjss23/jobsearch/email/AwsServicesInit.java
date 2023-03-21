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

    public void setupSns() {
        setupBounceEmailSns();
        setupComplaintEmailSns();
        setupDeliveredEmailSns();
    }

    private void setupBounceEmailSns() {
        var bounceEmailSes =
                CreateTopicRequest.builder().name("bounce_email_ses").build();

        snsClient.createTopic(bounceEmailSes);

        var bounceSubscription = SubscribeRequest.builder()
                .topicArn(bounceEmailSes.name())
                .protocol("http")
                .endpoint("http://host.docker.internal:8080/api/v1/email")
                .build();

        snsClient.subscribe(bounceSubscription);
    }

    private void setupComplaintEmailSns() {
        var complaintEmailSes =
                CreateTopicRequest.builder().name("complaint_email_ses").build();

        snsClient.createTopic(complaintEmailSes);

        var bounceSubscription = SubscribeRequest.builder()
                .topicArn(complaintEmailSes.name())
                .protocol("http")
                .endpoint("http://host.docker.internal:8080/api/v1/email")
                .build();

        snsClient.subscribe(bounceSubscription);
    }

    private void setupDeliveredEmailSns() {
        var deliveredEmailSes =
                CreateTopicRequest.builder().name("delivered_email_ses").build();

        snsClient.createTopic(deliveredEmailSes);

        var bounceSubscription = SubscribeRequest.builder()
                .topicArn(deliveredEmailSes.name())
                .protocol("http")
                .endpoint("http://host.docker.internal:8080/api/v1/email")
                .build();

        snsClient.subscribe(bounceSubscription);
    }

    public void setupSes() {
        var request = VerifyEmailIdentityRequest.builder()
                .emailAddress("noreply@jobsearch.com")
                .build();

        sesClient.verifyEmailIdentity(request);

        setupSesBounce();
        setupSesComplaint();
        setupSesDelivered();
    }

    private void setupSesBounce() {
        var bounce = SetIdentityNotificationTopicRequest.builder()
                .identity("noreply@jobsearch.com")
                .notificationType(NotificationType.BOUNCE)
                .build();

        sesClient.setIdentityNotificationTopic(bounce);
    }

    private void setupSesComplaint() {
        var complaint = SetIdentityNotificationTopicRequest.builder()
                .identity("noreply@jobsearch.com")
                .notificationType(NotificationType.COMPLAINT)
                .build();

        sesClient.setIdentityNotificationTopic(complaint);
    }

    private void setupSesDelivered() {
        var delivered = SetIdentityNotificationTopicRequest.builder()
                .identity("noreply@jobsearch.com")
                .notificationType(NotificationType.DELIVERY)
                .build();

        sesClient.setIdentityNotificationTopic(delivered);
    }

    public void setupS3() {
        var createBucket =
                CreateBucketRequest.builder().bucket("jobsearch-bucket").build();

        s3Client.createBucket(createBucket);
    }
}
