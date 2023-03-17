package com.fjss23.jobsearch.email.sns;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionRequest;
import software.amazon.awssdk.services.sns.model.ConfirmSubscriptionResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class PushNotificationService {

    private final SnsClient snsClient;

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    public PushNotificationService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    void confirmSub(String subscriptionToken, String topicArn) {
        try {
            ConfirmSubscriptionRequest request = ConfirmSubscriptionRequest.builder()
                    .token(subscriptionToken)
                    .topicArn(topicArn)
                    .build();

            ConfirmSubscriptionResponse result = snsClient.confirmSubscription(request);
            logger.info("\n\nStatus was " + result.sdkHttpResponse().statusCode()
                    + "\n\nSubscription Arn: \n\n"
                    + result.subscriptionArn());
        } catch (SnsException e) {
            logger.error("There is an error... {}", e.awsErrorDetails().errorMessage());
        }
    }

    SubscriptionConfirmation getReqInfo(String params) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SubscriptionConfirmation subInfo = objectMapper.readValue(params, SubscriptionConfirmation.class);
            return subInfo;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
