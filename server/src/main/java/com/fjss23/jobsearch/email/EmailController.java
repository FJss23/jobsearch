package com.fjss23.jobsearch.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionRequest;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

/*
* Important: https://docs.aws.amazon.com/sns/latest/dg/SendMessageToHttp.prepare.html
* You should check the appropriate headers from aws
 */
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final static Logger logger = LoggerFactory.getLogger(EmailController.class);
    private final SnsClient snsClient;

    public EmailController(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    @GetMapping("health")
    public void healthCheck() {
        logger.info("Receiving the health request");
    }

    @PostMapping("sns-bounce")
    public void snsEmailBounce(@RequestBody String request) {
        logger.info("Bounce-SNS email, {}", request);
        //confirmSub(requestDto.subscriptionToken(), requestDto.topicArn());
    }

    @PostMapping("sns-complaint")
    public void snsEmailComplaint(@RequestBody AwsSnsConfirmationRequestDto requestDto) {
        logger.info("Complaint-SNS email");
        confirmSub(requestDto.subscriptionToken(), requestDto.topicArn());
    }

    @PostMapping("sns-delivered")
    public void snsEmailDelivered(@RequestBody AwsSnsConfirmationRequestDto requestDto) {
        logger.info("Delivered-SNS email");
        confirmSub(requestDto.subscriptionToken(), requestDto.topicArn());
    }

    public void confirmSub(String subscriptionToken, String topicArn) {
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
}
