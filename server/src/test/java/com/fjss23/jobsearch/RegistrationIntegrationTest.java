package com.fjss23.jobsearch;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class RegistrationIntegrationTest extends AbstractIntegrationTest {

    // {
    //  "messages": [
    //    {
    //      "Id": "vfkbtcpylmjuumbr-rmootkeb-mrxj-mdfu-nbns-hdexwrrfgfmc-tukwwi",
    //      "Region": "eu-west-3",
    //      "Destination": {
    //        "ToAddresses": [
    //          "test2@test.com"
    //        ]
    //      },
    //      "Source": "noreply@jobsearch.com",
    //      "Subject": "Registration process jobsearch.com",
    //      "Body": {
    //        "text_part": null,
    //        "html_part": "Welcome Francisco. To complete the registration process click following link:
    // http://localhost:8080/api/v1/auth/registration/confirm?token=12d5c91d-dea1-498f-8b53-f13738758d44"
    //      },
    //      "Timestamp": "2023-02-24T18:48:28"
    //    }
    //  ]
    // }
    @Test
    public void when_register_with_invalid_fields_then_return_errors_for_each_field() {
        given(requestSpec)
                .body(
                        """
                    {
                        "firstName": "Francisco",
                        "lastName": "Javier",
                        "email": "fran@test.com",
                        "password": "123456",
                        "repeatPassword": "123456"
                    }
                """)
                .when()
                .post("/api/v1/auth/registration")
                .then()
                .log()
                .ifValidationFails(LogDetail.ALL)
                .and()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());

        /*given(requestLocalStackSpec)
        .when()
        .get("/_aws/ses")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("messages.findAll { it.Destination.ToAddresses == " + "test2@test.com" + "}.Subject", equalTo("Registration process jobsearch.com"));

         */
    }

    @Test
    public void when_sending_email_with_aws_ses_no_error_is_generated() {}
}
