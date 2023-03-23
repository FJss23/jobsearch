package com.fjss23.jobsearch.email.sns;

import com.fjss23.jobsearch.ApiV1PrefixController;
import com.fjss23.jobsearch.email.ses.EmailService;
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
public class PushNotificationController {

    private static final String CONFIRM_SUBSCRIPTION_HEADER = "x-amz-sns-message-type=SubscriptionConfirmation";
    private static final String NOTIFICATION_HEADER = "x-amz-sns-message-type=Notification";

    private final PushNotificationService pushNotificationService;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationController.class);

    public PushNotificationController(PushNotificationService pushNotificationService, EmailService emailService) {
        this.pushNotificationService = pushNotificationService;
        this.emailService = emailService;
    }

    @RequestMapping(path = "/email/sns-bounce", method = RequestMethod.POST, headers = NOTIFICATION_HEADER)
    public void snsBounceNotification(@RequestBody String params) {
        logger.info("params {}", params);
        logger.info("Bounce NOTIFICATION");
        //emailService.saveBounceNotification();
    }

    @RequestMapping(path = "/email/sns-complaint", method = RequestMethod.POST, headers = NOTIFICATION_HEADER)
    public void snsComplaintNotification(@RequestBody String params) {
        logger.info("params {}", params);
        logger.info("Complaint NOTIFICATION");
        //emailService.saveBounceNotification();
    }

    @RequestMapping(path = "/email/sns-delivered", method = RequestMethod.POST, headers = NOTIFICATION_HEADER)
    public void snsDeliveredNotification(@RequestBody String params) {
        logger.info("params {}", params);
        logger.info("Delivered NOTIFICATION");
        //emailService.saveBounceNotification();
    }

    @RequestMapping(
            path = {"/email/sns-bounce", "/email/sns-complaint", "/email/sns-delivered"},
            method = RequestMethod.POST,
            headers = CONFIRM_SUBSCRIPTION_HEADER)
    public void snsConfirmSubscriptionForSes(@RequestBody String params) {
        SubscriptionConfirmation subInfo = pushNotificationService.getReqInfo(params);
        pushNotificationService.confirmSub(subInfo.Token(), subInfo.TopicArn());
    }
}
