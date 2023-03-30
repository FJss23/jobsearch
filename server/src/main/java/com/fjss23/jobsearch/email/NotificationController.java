package com.fjss23.jobsearch.email;

import com.fjss23.jobsearch.ApiV1PrefixController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/*
 * - Important: https://docs.aws.amazon.com/sns/latest/dg/SendMessageToHttp.prepare.html
 * - The format of the body: https://docs.aws.amazon.com/sns/latest/dg/sns-message-and-json-formats.html
 * - TODO: Any one can send a request to these endpoints, better check a way to secure them
 * - AWS sends the confirmation in json string (content-type: text/plain)
 */
@ApiV1PrefixController
public class NotificationController {

    private static final String CONFIRM_SUBSCRIPTION_HEADER = "x-amz-sns-message-type=SubscriptionConfirmation";
    private static final String NOTIFICATION_HEADER = "x-amz-sns-message-type=Notification";

    private final NotificationService notificationService;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    public NotificationController(NotificationService notificationService, EmailService emailService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    /* Notification example
    {
      "Type": "Notification",
      "MessageId": "bbf97490-5681-4bfd-8df8-9b7780969b5a",
      "TopicArn": "arn:aws:sns:us-east-1:000000000000:email_ses_management",
      "Message": {
        "eventType": "Delivery",
        "mail": {
          "timestamp": "2023-03-23T19:01:47.881597+00:00",
          "source": "noreply@jobsearch.com",
          "sourceArn": "arn:aws:ses:us-east-1:000000000000:identity/noreply@jobsearch.com",
          "sendingAccountId": "000000000000",
          "destination": ["test@test.com"],
          "messageId": "hjoddhsoppebuovj-snouukoa-enpn-ouvk-mldk-zgjcqjryxoot-tjqjcw",
          "tags": {}
        },
        "delivery": {
          "recipients": ["test@test.com"],
          "timestamp": "2023-03-23T19:01:47.881597+00:00"
        }
      },
      "Timestamp": "2023-03-23T19:01:47.895Z",
      "SignatureVersion": "1",
      "Signature": "EXAMPLEpH+..",
      "SigningCertURL": "https://sns.us-east-1.amazonaws.com/SimpleNotificationService-0000000000000000000000.pem",
      "UnsubscribeURL": "http://localhost:4566/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:000000000000:email_ses_management:f952570b-d99a-4eef-bd5f-6ff9cb9218e3",
      "Subject": "Amazon SES Email Event Notification"
    }
     */
    @RequestMapping(path = "/email/notification-management", method = RequestMethod.POST, headers = NOTIFICATION_HEADER)
    public void snsBounceNotification(@RequestBody String params) {
        logger.info("params {}", params);
        //EmailNotification get
        //var notification = new EmailNotification();
        //emailService.saveNotification(notification);
    }

    @RequestMapping(
            path = "/email/notification-management",
            method = RequestMethod.POST,
            headers = CONFIRM_SUBSCRIPTION_HEADER)
    public void snsConfirmSubscriptionForSes(@RequestBody String params) {
        SubscriptionConfirmation subInfo = notificationService.getSubscriptionInfoFromRequest(params);
        notificationService.confirmSub(subInfo.Token(), subInfo.TopicArn());
    }
}
