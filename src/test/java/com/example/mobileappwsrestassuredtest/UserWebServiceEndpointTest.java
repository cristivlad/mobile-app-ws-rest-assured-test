package com.example.mobileappwsrestassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserWebServiceEndpointTest {

    private static final String EMAIL_ADDRESS = "valid@email.com";
    private static final String APPLICATION_JSON = "application/json";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;
    }

    @Test
    final void testLoginUser() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", EMAIL_ADDRESS);
        loginDetails.put("password", "123");

        Response response = given().contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(loginDetails).when()
                .post("/users/login")
                .then().statusCode(200).extract().response();

        String authorization = response.header("Authorization");
        String userId = response.header("UserID");

        assertNotNull(authorization);
        assertNotNull(userId);
    }
}
