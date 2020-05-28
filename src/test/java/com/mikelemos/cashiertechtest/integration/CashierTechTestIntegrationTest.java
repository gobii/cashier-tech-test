package com.mikelemos.cashiertechtest.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.with;


@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CashierTechTestIntegrationTest {

    @Test
    public void shouldReturn201ForValidUserRegistrationRequest() {

        with().
                body("{\"username\": \"BobFrench\", \"password\": \"Password1\", \"dob\": \"1980-02-21\", \"paymentCardNumber\": \"349293081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void shouldReturn403ForUnderAgeUserRegistrationRequest() {
        with().
                body("{\"username\": \"BobFrench\", \"password\": \"Password1\", \"dob\": \"2009-02-21\", \"paymentCardNumber\": \"349293081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void shouldReturn406ForCardPaymentINNBlockedUserRegistrationRequest() {
        with().
                body("{\"username\": \"BobFrench\", \"password\": \"Password1\", \"dob\": \"2000-02-21\", \"paymentCardNumber\": \"999999081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Test
    public void shouldReturn409ForUsernameAlreadyRegisteredUserRegistrationRequest() {
        with().
                body("{\"username\": \"BobFrench\", \"password\": \"Password1\", \"dob\": \"1980-02-21\", \"paymentCardNumber\": \"349293081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.CREATED.value());

        with().
                body("{\"username\": \"BobFrench\", \"password\": \"Password1\", \"dob\": \"1980-02-21\", \"paymentCardNumber\": \"349293081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void shouldReturn400ForBadPasswordFormatUserRegistrationRequest() {
        //No digit
        with().
                body("{\"username\": \"BobFrench\", \"password\": \"Password\", \"dob\": \"2000-02-21\", \"paymentCardNumber\": \"999999081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());

        //No upper case
        with().
                body("{\"username\": \"BobFrench\", \"password\": \"password1\", \"dob\": \"2000-02-21\", \"paymentCardNumber\": \"999999081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());

        //Less than 4 characters
        with().
                body("{\"username\": \"BobFrench\", \"password\": \"Pa1\", \"dob\": \"2000-02-21\", \"paymentCardNumber\": \"999999081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturn400ForBadUsernameFormatUserRegistrationRequest() {
        //With spaces
        with().
                body("{\"username\": \"Bob French\", \"password\": \"Password1\", \"dob\": \"2000-02-21\", \"paymentCardNumber\": \"999999081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturn400ForBadPaymentCardNumberFormatUserRegistrationRequest() {
        //too short - less than 15 characters
        with().
                body("{\"username\": \"Bob French\", \"password\": \"Password1\", \"dob\": \"2000-02-21\", \"paymentCardNumber\": \"111222333\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());

        //too long - more than 19 characters
        with().
                body("{\"username\": \"Bob French\", \"password\": \"Password1\", \"dob\": \"2000-02-21\", \"paymentCardNumber\": \"11124412412414112331222333\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldReturn400ForBadDOBFormatUserRegistrationRequest() {
        //not ISO DOB - should be yyyy-MM-dd
        with().
                body("{\"username\": \"Bob French\", \"password\": \"Password1\", \"dob\": \"2000-21-10\", \"paymentCardNumber\": \"999999081054422\"}").contentType(ContentType.JSON).
                when().
                post("http://localhost:8080/register").
                then().assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

}
