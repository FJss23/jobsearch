package com.fjss23.jobsearch.aws.sns;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SubscriptionConfirmationSns(
    String Token,
    String TopicArn,
    String SubscribeURL
) {
}
