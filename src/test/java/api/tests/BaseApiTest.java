package api.tests;

import api.client.RestAssuredClient;
import api.helpers.ResponseValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for API tests, providing setup, teardown, and response validation utilities.
 */
public class BaseApiTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseApiTest.class);
    protected RestAssuredClient client;

    /**
     * Performs setup before each test method.
     */
    @BeforeMethod
    public void setUp() {
        logger.info("Setting up API test for thread {}", Thread.currentThread().getName());
        client = new RestAssuredClient();
        logger.debug("Client initialized: {}", client);
    }

    /**
     * Performs cleanup after each test method.
     */
    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down API test for thread {}", Thread.currentThread().getName());
        client = null;
    }

    /**
     * Asserts that an API response matches the expected status code and JSON schema.
     * @param response The API response to validate
     * @param expectedStatus The expected HTTP status code (e.g., 200, 201)
     * @param schemaPath Path to the JSON schema file in classpath (e.g., "schemas/user-list-schema.json")
     */
    protected void assertResponse(Response response, int expectedStatus, String schemaPath) {
        logger.info("Asserting response: statusCode={}, schemaPath={}", expectedStatus, schemaPath);
        ResponseValidator.validateResponse(response, expectedStatus, schemaPath);
    }
}