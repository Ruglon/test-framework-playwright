package api.client;

import config.ApiConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes Rest Assured for API testing with configuration from ApiConfigManager.
 * Provides a reusable RequestSpecification for API requests with base URL, content type, and API key.
 */
public class RestAssuredClient {
    private static final Logger logger = LoggerFactory.getLogger(RestAssuredClient.class);
    private final RequestSpecification requestSpec;
    private static final String API_KEY = "reqres-free-v1";

    /**
     * Constructs a RestAssuredClient with configured base URL and API key.
     */
    public RestAssuredClient() {
        // Load API configuration
        ApiConfigManager config = ApiConfigManager.getInstance();
        String baseUrl = config.get("api.url");
        logger.debug("Base URL retrieved: {}", baseUrl);

        // Build request specification to match minimal test setup
        requestSpec = RestAssured.given()
                .baseUri(baseUrl)
                .header("X-API-Key", config.get("api.key"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .spec(new RequestSpecBuilder().build());

        // Enable logging for failed validations
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        logger.info("Initialized RestAssuredClient with base URL: {}", baseUrl);
        logger.debug("API Key added to headers: X-API-Key={}", API_KEY);

        // Debug: Verify requestSpec is not null
        if (requestSpec == null) {
            logger.error("RequestSpecification is null after initialization");
            throw new IllegalStateException("Failed to initialize RequestSpecification");
        }
    }

    /**
     * Returns the configured RequestSpecification for API requests.
     * @return RequestSpecification with base URL, content type, and API key
     */
    public RequestSpecification getRequestSpec() {
        logger.debug("Returning RequestSpecification: {}", requestSpec);
        return requestSpec;
    }
}