package com.fjss23.jobsearch.email.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/*
* - Important: https://docs.aws.amazon.com/sns/latest/dg/SendMessageToHttp.prepare.html
* - The format of the body: https://docs.aws.amazon.com/sns/latest/dg/sns-message-and-json-formats.html
* - TODO: Any one can send a request to these endpoints, better check a way to secure them
* - AWS sends the confirmation in json string (content-type: text/plain)
*/
@RestController
@RequestMapping("/api/v1/email")
public class PushNotificationController {

    private final static Logger logger = LoggerFactory.getLogger(PushNotificationController.class);

    private static final String CONFIRM_SUBSCRIPTION_HEADER = "x-amz-sns-message-type=SubscriptionConfirmation";
    private static final String NOTIFICATION_HEADER = "x-amz-sns-message-type=Notification";

    private final PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @GetMapping("health")
    public void healthCheck() {
        logger.info("Receiving the health request");
    }

    @RequestMapping(path = "sns-bounce", method = RequestMethod.POST, headers = NOTIFICATION_HEADER)
    public void snsBounceNotification(@RequestBody String params) {
        logger.info("Bounce NOTIFICATION");
    }

    @RequestMapping(path = "sns-complaint", method = RequestMethod.POST, headers = NOTIFICATION_HEADER)
    public void snsComplaintNotification(@RequestBody String params) {
        logger.info("Complaint NOTIFICATION");
    }

    @RequestMapping(path = "sns-delivered", method = RequestMethod.POST, headers = NOTIFICATION_HEADER)
    public void snsDeliveredNotification(@RequestBody String params) {
        logger.info("Delivered NOTIFICATION");
    }

    @RequestMapping(
        path = { "sns-bounce", "sns-complaint", "sns-delivered" },
        method = RequestMethod.POST,
        headers = CONFIRM_SUBSCRIPTION_HEADER
    )
    public void snsConfirmSubscriptionForSes(@RequestBody String params) {
        SubscriptionConfirmation subInfo = pushNotificationService.getReqInfo(params);
        pushNotificationService.confirmSub(subInfo.Token(), subInfo.TopicArn());
    }
}
