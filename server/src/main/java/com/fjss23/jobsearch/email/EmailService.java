package com.fjss23.jobsearch.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Some examples:
 * - https://docs.awspring.io/spring-cloud-aws/docs/2.4.1/reference/html/index.html#sending-mails
 * - https://www.baeldung.com/spring-email
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(
        EmailService.class
    );
    private JavaMailSender mailSender;

    private static final String EMAIL_NO_REPLY = "noreply@jobsearch.com";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
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

        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(EMAIL_NO_REPLY);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        logger.info("Email sent.\n {}", simpleMailMessage.toString());
        this.mailSender.send(simpleMailMessage);
    }
}
