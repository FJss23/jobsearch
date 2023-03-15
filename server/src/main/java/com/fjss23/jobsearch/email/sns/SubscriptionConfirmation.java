package com.fjss23.jobsearch.email.sns;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SubscriptionConfirmation(
    String Token,
    String TopicArn,
    String SubscribeURL
) {}
