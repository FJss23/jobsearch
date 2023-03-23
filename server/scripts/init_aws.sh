#!/bin/bash

# Remeber to make the file executable... chmod +x ./init_aws.sh
# Check https://docs.docker.com/desktop/networking/ for host.docker.internal
# IMPORTANT:
#   - The localstack container is using aws cli v1
#   - Localstack doesn't support sesv2
#   - aws ses set-identity-headers-in-notifications-enabled not supported by localstack
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

# Step 1: Create the topics
EMAIL_MANAGEMENT_ARN=$(aws sns create-topic \
    --name email_ses_management \
    --output text \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack \
    --query 'TopicArn')

# aws sns add-permission \
#     --topic-arn $BOUNCE_ARN \
#     --label public_permission_ses_bounce \
#     --aws-account-id 000000000000 \
#     --action-name Publish \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# COMPLAINT_ARN=$(aws sns create-topic \
#     --name complaint_email_ses \
#     --output text \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack \
#     --query 'TopicArn')

# aws sns add-permission \
#     --topic-arn $COMPLAINT_ARN \
#     --label public_permission_ses_complaint  \
#     --aws-account-id 000000000000 \
#     --action-name Publish \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# DELIVERED_ARN=$(aws sns create-topic \
#     --name delivered_email_ses \
#     --output text \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack \
#     --query 'TopicArn')

# aws sns add-permission \
#     --topic-arn $DELIVERED_ARN \
#     --label public_permission_ses_delivered   \
#     --aws-account-id 000000000000 \
#     --action-name Publish \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack


# Step 2: Create a subscription of the topic
BASE_URL="http://host.docker.internal:8080/api/v1/email"

aws sns subscribe \
    --topic-arn $EMAIL_MANAGEMENT_ARN \
    --protocol http \
    --notification-endpoint $BASE_URL/sns-bounce \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

# aws sns subscribe \
#     --topic-arn $COMPLAINT_ARN  \
#     --protocol http \
#     --notification-endpoint $BASE_URL/sns-complaint \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# aws sns subscribe \
#     --topic-arn $DELIVERED_ARN \
#     --protocol http \
#     --notification-endpoint $BASE_URL/sns-delivered \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# Step : Confirm the topics were created
aws sns list-topics \
    --endpoint-url http://localhost:4566 \
    --region eu-west-3 \
    --profile localstack


# ----------------------- SES -----------------------
echo -e "\n=========== SES ===========\n"

# Step 1: Verify the email address
BASE_EMAIL="noreply@jobsearch.com"

# aws sesv2 create-email-identity \
#     --email-identity $BASE_EMAIL \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

aws ses verify-email-identity \
    --email-address $BASE_EMAIL \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

aws ses create-configuration-set \
    --configuration-set "{\"Name\":\"ses_config_set\"}" \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

aws ses create-configuration-set-event-destination \
    --configuration-set-name ses_config_set \
    --event-destination "{\"Name\":\"some_name2\",\"Enabled\":true,\"MatchingEventTypes\":[\"send\",\"bounce\",\"delivery\",\"open\",\"click\"],\"SNSDestination\":{\"TopicARN\":\"$EMAIL_MANAGEMENT_ARN\"}}" \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

# Step 2: Susbscribe to the topics
# aws ses set-identity-notification-topic \
#     --identity $BASE_EMAIL \
#     --notification-type Bounce \
#     --sns-topic $BOUNCE_ARN \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# NOT SUPPORTED BY LOCALSTACK
# aws ses set-identity-headers-in-notifications-enabled \
#     --identity $BASE_EMAIL \
#     --notification-type Bounce \
#     --enabled \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# aws ses set-identity-notification-topic \
#     --identity $BASE_EMAIL \
#     --notification-type Complaint \
#     --sns-topic $COMPLAINT_ARN \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# NOT SUPPORTED BY LOCALSTACK
# aws ses set-identity-headers-in-notifications-enabled \
#     --identity $BASE_EMAIL \
#     --notification-type Complaint \
#     --enabled \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# aws ses set-identity-notification-topic \
#     --identity $BASE_EMAIL \
#     --notification-type Delivery \
#     --sns-topic $DELIVERED_ARN \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# NOT SUPPORTED BY LOCALSTACK
# aws ses set-identity-headers-in-notifications-enabled \
#     --identity $BASE_EMAIL \
#     --notification-type Delivery \
#     --enabled \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# Step 3: Check if the email was correctly verified & more
# aws ses list-identities \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

# TODO:
# - Try using create-configuration-set-event-destination
# - Try aws cli version 2 inside the container (maybe with awslocal)
# - Check /_aws/sns and see if there is something after sending an email
# - Also check https://github.com/localstack/localstack/issues/7323

aws ses send-email \
    --from "noreply@jobsearch.com" \
    --to "test@test.com" \
    --subject "Just a subject" \
    --text "This is just an email" \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

# ----------------------- S3 -----------------------
echo -e "\n=========== S3 ===========\n"

# Step 1: Create the bucket
aws s3 mb s3://jobsearch-bucket \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

# Step 2: Confirm if it was created
aws s3 ls \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

# --------------------------------------------------
# aws sns publish \
#     --topic-arn $EMAIL_MANAGEMENT_ARN  \
#     --message "Hello World!" \
#     --region eu-west-3 \
#     --endpoint-url http://localhost:4566 \
#     --profile localstack

