package com.example.mobileappwsrestassuredtest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserWebServiceEndpointTest {

    private static final String EMAIL_ADDRESS = "valid@email.com";
    private static final String APPLICATION_JSON = "application/json";
    private static String authorization;
    private static String userId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;
    }

    @Test
    @Order(1)
    final void testLoginUser() {
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", EMAIL_ADDRESS);
        loginDetails.put("password", "123");

        Response response = given().contentType(APPLICATION_JSON).accept(APPLICATION_JSON)
                .body(loginDetails).when()
                .post("/users/login")
                .then().statusCode(200).extract().response();

        authorization = response.header("Authorization");
        userId = response.header("UserID");

        assertNotNull(authorization);
        assertNotNull(userId);
    }

    @Test
    @Order(2)
    final void testGetUserDetails() {
        Response response = given().pathParam("userId", userId).header("Authorization", authorization).accept(APPLICATION_JSON).when()
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .contentType(APPLICATION_JSON)
                .extract().response();

        String userPublicId = response.jsonPath().getString("userId");
        String userEmail = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");
        List<Map<String, String>> addresses = response.jsonPath().getList("addresses");
        String addressId = addresses.get(0).get("addressId");

        assertNotNull(userPublicId);
        assertNotNull(userEmail);
        assertNotNull(firstName);
        assertNotNull(lastName);
        assertEquals(EMAIL_ADDRESS, userEmail);
        assertEquals(1, addresses.size());
        assertEquals(30, addressId.length());
    }
}
