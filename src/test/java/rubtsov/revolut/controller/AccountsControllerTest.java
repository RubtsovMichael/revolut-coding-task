package rubtsov.revolut.controller;

import io.restassured.RestAssured;
import org.glassfish.grizzly.http.server.HttpServer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rubtsov.revolut.application.App;

import static io.restassured.RestAssured.when;

class AccountsControllerTest {

    private static HttpServer httpServer;

    @BeforeAll
    static void setUp() {
        httpServer = App.startServer();
        RestAssured.baseURI = App.BASE_URI;
    }

    @AfterAll
    static void tearDown() {
        httpServer.shutdownNow();
    }

    @Test
    void passesSanityCheck() {
        when()
                .get("/accounts/sanity")
        .then()
                .statusCode(200)
                .body(Matchers.equalTo("hello"));
    }

}