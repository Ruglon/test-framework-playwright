package api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Minimal test class to debug Rest Assured request execution.
 */
public class MinimalRestAssuredTest {
    private static final Logger logger = LoggerFactory.getLogger(MinimalRestAssuredTest.class);
    private static final String BASE_URL = "https://reqres.in/api";
    private static final String API_KEY = "reqres-free-v1";

    @Test
    public void testMinimalRequest() {
        logger.info("Starting minimal Rest Assured request test");
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("X-API-Key", API_KEY)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("page", 2)
                .when()
                .get("/users");
        logger.info("Response received: StatusCode={}, Body={}", response.getStatusCode(), response.asString());
    }
}