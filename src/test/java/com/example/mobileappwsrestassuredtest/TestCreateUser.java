package com.example.mobileappwsrestassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestCreateUser {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;
    }

    @Test
    final void testCreateUser() {
        List<Map<String,Object>> userAddresses = new ArrayList<>();
        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Bucharest");
        shippingAddress.put("country", "Romania");
        shippingAddress.put("streetName", "street");
        shippingAddress.put("postalCode", "1234");
        shippingAddress.put("type", "shipping");

        userAddresses.add(shippingAddress);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Cristi");
        userDetails.put("lastName", "Cristi");
        userDetails.put("email", "valid@email.com");
        userDetails.put("password", "123");
        userDetails.put("addresses", userAddresses);

        Response response = given().contentType("application/json").accept("application/json")
                .body(userDetails).when().post("/users").then()
                .statusCode(200)
                .contentType("application/json").extract().response();

        String userId = response.jsonPath().getString("userId");
        assertNotNull(userId);
    }
}
