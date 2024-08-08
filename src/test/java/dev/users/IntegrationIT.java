package dev.users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;


@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class IntegrationIT {

    @Test
    @Order(1)
    public void testCreateUserSuccess() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "ricardo")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "password123")
                .when()
                .post("/api/user/create")
                .then()
                .statusCode(200)
                .body("username", is("ricardo"))
                .body("email", is("ricardo@example.com"));
    }

    @Test
    @Order(2)
    public void testCreateUserEmptyUsername() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "password123")
                .when()
                .post("/api/user/create")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(3)
    public void testCreateUserEmptyEmail() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "ricardo")
                .formParam("email", "")
                .formParam("password", "password123")
                .when()
                .post("/api/user/create")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    public void testCreateUserEmptyPassword() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "ricardo")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "")
                .when()
                .post("/api/user/create")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(5)
    public void testCreateUserInvalidEmail() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "ricardo")
                .formParam("email", "invalidmail")
                .formParam("password", "password123")
                .when()
                .post("/api/user/create")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(6)
    public void testCreateUserPasswordTooShort() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "ricardo")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "short")
                .when()
                .post("/api/user/create")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(7)
    public void testSuccess() {

        String response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "password123")
                .when()
                .post("/api/user/authenticate")
                .getBody().asString();
        System.out.println(response);

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "password123")
                .when()
                .post("/api/user/authenticate")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(8)
    public void testEmptyEmail() {

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", "")
                .formParam("password", "password123")
                .when()
                .post("/api/user/authenticate")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(9)
    public void testInvalidEmail() {

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", "invalid-email")
                .formParam("password", "password123")
                .when()
                .post("/api/user/authenticate")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(10)
    public void testEmptyPassword() {

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "")
                .when()
                .post("/api/user/authenticate")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(11)
    public void testWrongPassword() {

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", "ricardo@example.com")
                .formParam("password", "wrong-password")
                .when()
                .post("/api/user/authenticate")
                .then()
                .statusCode(400);
    }
}
