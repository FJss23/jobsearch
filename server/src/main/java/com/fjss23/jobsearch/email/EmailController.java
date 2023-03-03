package com.fjss23.jobsearch.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/*
* - Important: https://docs.aws.amazon.com/sns/latest/dg/SendMessageToHttp.prepare.html
* - The format of the body: https://docs.aws.amazon.com/sns/latest/dg/sns-message-and-json-formats.html
* - You should check the appropriate headers from aws.
* - AWS sends the confirmation in json string (content-type: text/plain)
*/
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final static Logger logger = LoggerFactory.getLogger(EmailController.class);
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("health")
    public void healthCheck() {
        logger.info("Receiving the health request");
    }

    @PostMapping("sns-bounce")
    public void snsEmailBounce(@RequestBody String params) {
        SubscriptionConfirmationSns subInfo = emailService.getReqInfo(params);
        logger.info("Bounce-SNS email. Token: {}. Topic ARN: {}", subInfo.Token(), subInfo.TopicArn());
        emailService.confirmSub(subInfo.Token(), subInfo.TopicArn());
    }

    @PostMapping("sns-complaint")
    public void snsEmailComplaint(@RequestBody String params) {
        SubscriptionConfirmationSns subInfo = emailService.getReqInfo(params);
        logger.info("Complaint-SNS email. Token: {}. Topic ARN: {}", subInfo.Token(), subInfo.TopicArn());
        emailService.confirmSub(subInfo.Token(), subInfo.TopicArn());
    }

    @PostMapping("sns-delivered")
    public void snsEmailDelivered(@RequestBody String params) {
        SubscriptionConfirmationSns subInfo = emailService.getReqInfo(params);
        logger.info("Delivered-SNS email. Token: {}. Topic ARN: {}", subInfo.Token(), subInfo.TopicArn());
        emailService.confirmSub(subInfo.Token(), subInfo.TopicArn());
    }

}
