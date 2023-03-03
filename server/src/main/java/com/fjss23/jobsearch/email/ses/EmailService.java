package com.fjss23.jobsearch.aws.ses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjss23.jobsearch.aws.sns.SubscriptionConfirmationSns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionRequest;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(
        EmailService.class
    );

    private final SnsClient snsClient;
    private final SesClient sesClient;
    private static final String EMAIL_NO_REPLY = "noreply@jobsearch.com";

    public EmailService(SnsClient snsClient, SesClient sesClient) {
        this.snsClient = snsClient;
        this.sesClient = sesClient;
    }

    public void sendToken(
        String firstName,
        String to,
        String confirmationLink
    ) {
        // TODO: Include internationalization...
        logger.info(
            "Sending token to {} with confirmation link {}.",
            to,
            confirmationLink
        );
        var subject = "Registration process jobsearch.com";
        var message =
            "Welcome " +
            firstName +
            ". To complete the registration process click following link: " +
            confirmationLink;

        this.send(this.EMAIL_NO_REPLY, to, subject, message);
    }

    private void send(
        String from,
        String to,
        String subject,
        String message
    ) {

        try {
            logger.info(
                "Attempting to send an email through Amazon SES " +
                "using the AWS SDK for Java..."
            );

            Destination destination = Destination.builder()
                .toAddresses(to)
                .build();

            Content content = Content.builder()
                .data(message)
                .build();

            Content sub = Content.builder()
                .data(subject)
                .build();

            Body body = Body.builder()
                .html(content)
                .build();

            Message msg = Message.builder()
                .subject(sub)
                .body(body)
                .build();

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .message(msg)
                .source(from)
                .build();

            sesClient.sendEmail(emailRequest);
            logger.info("email was sent");
        } catch (Exception e) {
            logger.error("Couldn't send the email \n {}", e);
        }
    }


    void confirmSub(String subscriptionToken, String topicArn) {
        try {
            ConfirmSubscriptionRequest request = ConfirmSubscriptionRequest.builder()
                .token(subscriptionToken)
                .topicArn(topicArn)
                .build();

            ConfirmSubscriptionResponse result = snsClient.confirmSubscription(request);
            logger.info("\n\nStatus was " + result.sdkHttpResponse().statusCode() + "\n\nSubscription Arn: \n\n" + result.subscriptionArn());
        } catch (SnsException e) {
            logger.error("There is an error... {}", e.awsErrorDetails().errorMessage());
        }
    }

    SubscriptionConfirmationSns getReqInfo(String params) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SubscriptionConfirmationSns subInfo = objectMapper.readValue(params, SubscriptionConfirmationSns.class);
            return subInfo;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
