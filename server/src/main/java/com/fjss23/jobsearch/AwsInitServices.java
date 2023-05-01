package com.fjss23.jobsearch;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;

@Component
public class AwsInitServices {
    private final SnsClient snsClient;
    private final SesClient sesClient;

    public AwsInitServices(SnsClient snsClient, SesClient sesClient) {
        this.snsClient = snsClient;
        this.sesClient = sesClient;
    }
}
