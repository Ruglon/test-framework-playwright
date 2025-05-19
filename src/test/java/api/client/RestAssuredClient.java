package api.client;

import config.ApiConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a thread-safe Rest Assured client for API requests with centralized configuration.
 */
public class RestAssuredClient {
    private static final Logger logger = LoggerFactory.getLogger(RestAssuredClient.class);
    private static final ApiConfigManager config = ApiConfigManager.getInstance();

    /**
     * Creates a new RequestSpecification for an API request.
     * @param contentType The content type for the request (e.g., ContentType.JSON)
     * @return A configured RequestSpecification
     */
    public RequestSpecification createRequestSpec(ContentType contentType) {
        logger.debug("Creating RequestSpecification for thread {}", Thread.currentThread().getName());
        RequestSpecification spec = RestAssured.given()
                .baseUri(config.get("api.url"))
                .header("X-API-Key", config.get("api.key"))
                .contentType(contentType != null ? contentType : ContentType.JSON)
                .accept(ContentType.JSON);

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        logger.debug("RequestSpecification created: {}", spec);
        return spec;
    }

    /**
     * Creates a new RequestSpecification with default JSON content type.
     * @return A configured RequestSpecification
     */
    public RequestSpecification createRequestSpec() {
        return createRequestSpec(ContentType.JSON);
    }
}