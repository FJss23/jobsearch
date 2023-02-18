#!/bin/bash

aws --endpoint-url=http://localhost:4566 s3 mb s3://jobsearch_bucket

aws ses verirify-email-identity --email-address noreply@jobsearch.com --endpoint-url=http://localhost:4566
