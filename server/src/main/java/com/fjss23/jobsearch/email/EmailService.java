package com.fjss23.jobsearch.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(
        EmailService.class
    );

    private final SesClient sesClient;
    private static final String EMAIL_NO_REPLY = "noreply@jobsearch.com";

    public EmailService(SesClient sesClient) {
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
        String sender,
        String recipient,
        String subject,
        String bodyHTML
    ) {

        try {
            logger.info(
                "Attempting to send an email through Amazon SES " +
                "using the AWS SDK for Java..."
            );

            Destination destination = Destination.builder()
                .toAddresses(recipient)
                .build();

            Content content = Content.builder()
                .data(bodyHTML)
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
                .source(sender)
                .build();

            sesClient.sendEmail(emailRequest);
            logger.info("email was sent");
        } catch (Exception e) {
            logger.error("Couldn't send the email \n {}", e);
        }
    }
}
