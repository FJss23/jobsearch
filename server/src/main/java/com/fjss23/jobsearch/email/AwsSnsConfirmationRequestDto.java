package com.fjss23.jobsearch.email;

public record AwsSnsConfirmationRequestDto(
    String subscriptionToken,
    String topicArn
) { }
