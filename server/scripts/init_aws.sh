#!/bin/bash

# Remeber to make the file executable... chmod +x ./init_aws.sh
# Check https://docs.docker.com/desktop/networking/ for host.docker.internal
# IMPORTANT:
#   - The localstack container is using aws cli v1
#   - Localstack doesn't support sesv2 (aws sesv2 create-email-identity, etc.)
#   - aws ses set-identity-headers-in-notifications-enabled not supported by localstack,
#       but it can be interesting to use in production.
#   - Verifying en email addres requires having access to that email and clicking a link
#       that aws will send in the verification process, if we are using a domain a dkim
#       will be required.
#   - aws ses set-identity-notification-topic can also be appropiate, but
#       create-configuration-set-event-destination its more powerful
#   - Using awslocal instead aws allow us to not include the --endpoint-url flag
#   - At this point its very clear to me, that I will need a different script in production
# Some interesting reads:
#   - https://docs.aws.amazon.com/ses/latest/dg/configure-sns-notifications.html

# ----------------------- CONFIG -----------------------
echo -e "\n====== Configuration ======\n"

aws configure set aws_access_key_id foo \
    --profile localstack

aws configure set aws_secret_access_key bar \
    --profile localstack

aws configure set region eu-west-3 \
    --profile localstack

aws configure set output json \
    --profile localstack

aws configure list --profile localstack

export AWS_PROFILE=localstack

# ----------------------- SNS -----------------------
echo -e "\n=========== SNS ===========\n"

EMAIL_MANAGEMENT_ARN=$(aws sns create-topic \
    --name email_ses_management \
    --output text \
    --endpoint-url http://localhost:4566 \
    --query 'TopicArn')

aws sns add-permission \
    --topic-arn $EMAIL_MANAGEMENT_ARN \
    --label public_permission_ses_management \
    --aws-account-id 000000000000 \
    --endpoint-url http://localhost:4566 \
    --action-name Publish

aws sns subscribe \
    --topic-arn $EMAIL_MANAGEMENT_ARN \
    --protocol http \
    --endpoint-url http://localhost:4566 \
    --notification-endpoint "http://host.docker.internal:8080/api/v1/email/notification-management"

aws sns list-topics \
    --endpoint-url http://localhost:4566

# ----------------------- SES -----------------------
echo -e "\n=========== SES ===========\n"

aws ses verify-email-identity \
    --email-address noreply@jobsearch.com \
    --endpoint-url http://localhost:4566


aws ses create-configuration-set \
    --configuration-set "{\"Name\":\"ses_config_set\"}" \
    --endpoint-url http://localhost:4566

aws ses create-configuration-set-event-destination \
    --configuration-set-name ses_config_set \
    --event-destination "{\"Name\":\"some_name2\",\"Enabled\":true,\"MatchingEventTypes\":[\"send\",\"bounce\",\"delivery\",\"open\",\"click\"],\"SNSDestination\":{\"TopicARN\":\"$EMAIL_MANAGEMENT_ARN\"}}" \
    --endpoint-url http://localhost:4566

aws ses send-email \
    --from noreply@jobsearch.com \
    --to test@test.com \
    --subject "Hello World" \
    --text "Hello World! This is your first message!" \
    --endpoint-url http://localhost:4566
