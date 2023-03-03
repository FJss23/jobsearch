#!/bin/bash

# Remeber to make the file executable... chmod +x ./init_aws.sh
# Check https://docs.docker.com/desktop/networking/ for host.docker.internal

# ----------------------- CONFIG -----------------------
echo -e "\n====== Configuration ======\n"

aws configure set aws_access_key_id foo \
    --profile localstack

aws configure set aws_secret_access_key bar \
    --profile localstack

aws configure set region eu-west-3 \
    --profile localstack

aws configure list --profile localstack

# ----------------------- SNS -----------------------
echo -e "\n=========== SNS ===========\n"

# Step 1: Create the topics
BOUNCE_ARN=$(aws sns create-topic \
    --name bounce_email_ses \
    --region eu-west-3 \
    --output text \
    --endpoint-url http://localhost:4566 \
    --profile localstack \
    --query 'TopicArn')

# COMPLAINT_ARN=$(awslocal sns create-topic \
#     --name complaint_email_ses \
#     --region eu-west-3 \
#     --output text \
#     --query 'TopicArn')

# DELIVERED_ARN=$(awslocal sns create-topic \
#     --name delivered_email_ses \
#     --region eu-west-3 \
#     --output text \
#     --query 'TopicArn')

# Step 2: Create a subscription of the topic
BASE_URL="http://host.docker.internal:8080/api/v1/email"

aws sns subscribe \
    --topic-arn $BOUNCE_ARN \
    --protocol http \
    --notification-endpoint $BASE_URL/sns-bounce \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566 \
    --profile localstack

# awslocal sns subscribe \
#     --topic-arn $COMPLAINT_ARN  \
#     --protocol http \
#     --notification-endpoint $BASE_URL/sns-complaint \
#     --region eu-west-3

# awslocal sns subscribe \
#     --topic-arn $DELIVERED_ARN \
#     --protocol http \
#     --notification-endpoint $BASE_URL/sns-delivered \
#     --region eu-west-3

# Step : Confirm the topics were created
# awslocal sns list-topics

# ----------------------- SES -----------------------
# echo -e "\n=========== SES ===========\n"

# Step 1: Verify the email address
# BASE_EMAIL="noreply@jobsearch.com"
#
# awslocal ses verify-email-identity \
#     --email-address $BASE_EMAIL
#
# Step 2: Susbscribe to the topics
# awslocal ses set-identity-notification-topic \
#     --identity $BASE_EMAIL \
#     --notification-type Bounce \
#     --sns-topic bounce_email_ses
#
# awslocal ses set-identity-notification-topic \
#     --identity $BASE_EMAIL \
#     --notification-type Complaint \
#     --sns-topic complaint_email_ses
#
# awslocal ses set-identity-notification-topic \
#     --identity $BASE_EMAIL \
#     --notification-type Delivery \
#     --sns-topic delivered_email_ses
#
# Step 3: Check if the email was correctly verified
# awslocal ses list-identities

# ----------------------- S3 -----------------------
# echo -e "\n=========== S3 ===========\n"

# Step 1: Create the bucket
# awslocal s3 mb s3://jobsearch-bucket

# Step 2: Confirm if it was created
# awslocal s3 ls

