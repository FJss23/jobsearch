package com.fjss23.jobsearch;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.NotificationType;
import software.amazon.awssdk.services.ses.model.SetIdentityNotificationTopicRequest;
import software.amazon.awssdk.services.ses.model.VerifyEmailIdentityRequest;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIntegrationTest {

    protected RequestSpecification requestSpec;
    protected RequestSpecification requestLocalStackSpec;

    private static final Logger logger = LoggerFactory.getLogger(AbstractIntegrationTest.class);

    @LocalServerPort
    protected int localServerPort;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1-alpine")
            .withFileSystemBind("./scripts/init_schema.sql", "/docker-entrypoint-initdb.d/init_schema.sql");

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.4"))
            .withLogConsumer(new Slf4jLogConsumer(logger))
            .withServices(
                    LocalStackContainer.Service.S3, LocalStackContainer.Service.SES, LocalStackContainer.Service.SNS);

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:7.0.8-alpine"));

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getExposedPorts);
    }

    public void setupAwsServices() {
        setupSns();
        setupSes();
        setupS3();
    }

    @BeforeEach
    public void setUpAbstractIntegrationTest() {
        setupAwsServices();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpec = new RequestSpecBuilder()
                .setPort(7777)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        requestLocalStackSpec = new RequestSpecBuilder()
                .setPort(4566)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    SesClient sesClient() {
        return SesClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.SES))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    SnsClient snsClient() {
        return SnsClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.SNS))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    public void setupSns() {
        setupBounceEmailSns();
        setupComplaintEmailSns();
        setupDeliveredEmailSns();
    }

    private void setupBounceEmailSns() {
        var bounceEmailSes =
                CreateTopicRequest.builder().name("bounce_email_ses").build();

        String bounceEmailSnsTopic = snsClient().createTopic(bounceEmailSes).topicArn();

        var bounceSubscription = SubscribeRequest.builder()
                .topicArn(bounceEmailSnsTopic)
                .protocol("http")
                .endpoint("http://host.testcontainers.internal:" + localServerPort + "/api/v1/email/sns-bounce")
                .build();

        snsClient().subscribe(bounceSubscription);
    }

    private void setupComplaintEmailSns() {
        var complaintEmailSes =
                CreateTopicRequest.builder().name("complaint_email_ses").build();

        String complaintEmailSnsTopic = snsClient().createTopic(complaintEmailSes).topicArn();

        var bounceSubscription = SubscribeRequest.builder()
                .topicArn(complaintEmailSnsTopic)
                .protocol("http")
                .endpoint("http://host.testcontainers.internal:" + localServerPort + "/api/v1/email/sns-complaint")
                .build();

        snsClient().subscribe(bounceSubscription);
    }

    private void setupDeliveredEmailSns() {
        var deliveredEmailSes =
                CreateTopicRequest.builder().name("delivered_email_ses").build();

        String deliveredEmailSnsTopic = snsClient().createTopic(deliveredEmailSes).topicArn();

        var bounceSubscription = SubscribeRequest.builder()
                .topicArn(deliveredEmailSnsTopic)
                .protocol("http")
                .endpoint("http://host.testcontainers.internal:" + localServerPort + "/api/v1/email/sns-delivered")
                .build();

        snsClient().subscribe(bounceSubscription);
    }

    public void setupSes() {
        var request = VerifyEmailIdentityRequest.builder()
                .emailAddress("noreply@jobsearch.com")
                .build();

        sesClient().verifyEmailIdentity(request);

        setupSesBounce();
        setupSesComplaint();
        setupSesDelivered();
    }

    private void setupSesBounce() {
        var bounce = SetIdentityNotificationTopicRequest.builder()
                .identity("noreply@jobsearch.com")
                .snsTopic("bounce_email_ses")
                .notificationType(NotificationType.BOUNCE)
                .build();

        sesClient().setIdentityNotificationTopic(bounce);
    }

    private void setupSesComplaint() {
        var complaint = SetIdentityNotificationTopicRequest.builder()
                .identity("noreply@jobsearch.com")
                .snsTopic("complaint_email_ses")
                .notificationType(NotificationType.COMPLAINT)
                .build();

        sesClient().setIdentityNotificationTopic(complaint);
    }

    private void setupSesDelivered() {
        var delivered = SetIdentityNotificationTopicRequest.builder()
                .identity("noreply@jobsearch.com")
                .snsTopic("delivered_email_ses")
                .notificationType(NotificationType.DELIVERY)
                .build();

        sesClient().setIdentityNotificationTopic(delivered);
    }

    public void setupS3() {
        var createBucket =
                CreateBucketRequest.builder().bucket("jobsearch-bucket").build();

        s3Client().createBucket(createBucket);
    }
}
