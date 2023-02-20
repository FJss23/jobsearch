#!/bin/bash
# Remeber to make the file executable... chmod +x ./init_aws.sh
#
# ----------------------- SES -----------------------

# Step 1: Verify the email address
aws ses verify-email-identity --email-address noreply@jobsearch.com \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566

# Step 2: Check if the email was correctly verified
aws ses list-identities \
    --region eu-west-3 \
    --endpoint-url http://localhost:4566

# ----------------------- S3 -----------------------
# Step 1: Create the bucket
# aws --endpoint-url=http://localhost:4566 s3 mb s3://jobsearch_bucket
#
# Step 2: Confirm if it was created
# aws --endpoint-url=http://localhost:4566 s3 ls
