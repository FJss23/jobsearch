package com.fjss23.jobsearch;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIntegrationTest {

    protected RequestSpecification requestSpec;
    protected RequestSpecification requestLocalStackSpec;

    @LocalServerPort
    protected int localServerPort;

   @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1-alpine")
        .withFileSystemBind("./scripts/init_schema.sql", "/docker-entrypoint-initdb.d/init_schema.sql");

   @Rule
    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.4"))
       .withFileSystemBind("./scripts/init_aws.sh", "/etc/localstack/init/ready.d/init_aws.sh")
       .withServices(LocalStackContainer.Service.SES);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUpAbstractIntegrationTest() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpec = new RequestSpecBuilder()
            .setPort(localServerPort)
            .addHeader(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE
            )
            .build();

        requestLocalStackSpec = new RequestSpecBuilder()
            .setPort(4566)
            .addHeader(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE
            )
            .build();
    }

    SesClient sesClient() {
        return SesClient.builder()
            .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.SES))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        localStack.getAccessKey(), localStack.getSecretKey()
                    )
                )
            )
            .region(Region.of(localStack.getRegion()))
            .build();
    }
}
