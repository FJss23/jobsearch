package com.fjss23.jobsearch.email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonKey;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SubscriptionConfirmationSns(
    String Token,
    String TopicArn,
    String SubscribeURL
) {
}
