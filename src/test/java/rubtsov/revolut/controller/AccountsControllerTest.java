package rubtsov.revolut.controller;

import io.restassured.RestAssured;
import org.glassfish.grizzly.http.server.HttpServer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rubtsov.revolut.application.App;
import rubtsov.revolut.application.AppConfig;
import rubtsov.revolut.logics.AccountsService;
import rubtsov.revolut.logics.InMemoryAccountsRepository;
import rubtsov.revolut.model.Account;
import rubtsov.revolut.model.TransferOrder;

import java.math.BigDecimal;

import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

class AccountsControllerTest {

    private static HttpServer httpServer;
    private static final InMemoryAccountsRepository repository = new InMemoryAccountsRepository();

    @BeforeAll
    static void setUp() {
        repository.create(new Account("111", new BigDecimal("123.12")));
        repository.create(new Account("222", new BigDecimal("123.12")));
        repository.create(new Account("333", BigDecimal.ZERO));
        repository.create(new Account("444", BigDecimal.ZERO));

        httpServer = App.startServer(new AppConfig(new AccountsService(repository)));
        RestAssured.baseURI = App.BASE_URI;
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
    }

    @AfterAll
    static void tearDown() {
        httpServer.shutdownNow();
    }

    @Test
    void badRequestForAbsentAccount() {
        when()
                .get("/accounts/123456")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("Account not found"));
    }

    @Test
    void findsAccountByNumber() {
        when()
                .get("/accounts/111")
                .then()
                .statusCode(200)
                .body("number", equalTo("111"))
                .body("amount", is(new BigDecimal("123.12")));
    }

    @Test
    void transferWorks() {
        given()
                .contentType(JSON)
                .body(TransferOrder.builder().from("222").to("333").amount(new BigDecimal("12.56")).build())
        .when()
                .post("/makeTransfer")
                .then()
                .statusCode(200);

       get("/accounts/222").then().body("amount", is(new BigDecimal("110.56")));
       get("/accounts/333").then().body("amount", is(new BigDecimal("12.56")));
    }

    @Test
    void transferFailsIfNotEnoughFunds() {
        given()
                .contentType(JSON)
                .body(TransferOrder.builder().from("444").to("222").amount(BigDecimal.ONE).build())
                .when()
                .post("/makeTransfer")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("Not enough funds <0.00> for requested transfer of 1.00"));
    }

    @Test
    void transferFailsIfAccountNotFound() {
        given()
                .contentType(JSON)
                .body(TransferOrder.builder().from("555").to("222").amount(BigDecimal.ONE).build())
                .when()
                .post("/makeTransfer")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("Account 555 not found"));
    }

}