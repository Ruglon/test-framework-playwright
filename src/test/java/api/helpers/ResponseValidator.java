package api.helpers;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Provides utility methods to validate API responses for status codes and JSON schema compliance.
 */
public class ResponseValidator {
    private static final Logger logger = LoggerFactory.getLogger(ResponseValidator.class);

    /**
     * Validates an API response against an expected status code and JSON schema.
     * @param response The API response to validate
     * @param expectedStatus The expected HTTP status code (e.g., 200, 201)
     * @param schemaPath Path to the JSON schema file in classpath (e.g., "schemas/user-list-schema.json")
     */
    public static void validateResponse(Response response, int expectedStatus, String schemaPath) {
        logger.info("Validating response: statusCode={}, schemaPath={}", expectedStatus, schemaPath);
        try {
            response.then()
                    .statusCode(expectedStatus)
                    .body(matchesJsonSchemaInClasspath(schemaPath));
            logger.info("Response validated successfully");
        } catch (Exception e) {
            logger.error("Failed to validate response: {}", e.getMessage());
            logger.debug("Schema path attempted: {}", schemaPath);
            logger.debug("Response body: {}", response.asString());
            throw new RuntimeException("Response validation failed", e);
        }
    }

    /**
     * Validates an API response against an expected status code only.
     * @param response The API response to validate
     * @param expectedStatus The expected HTTP status code (e.g., 200, 404)
     */
    public static void validateStatusCode(Response response, int expectedStatus) {
        logger.info("Validating status code: expected={}", expectedStatus);
        response.then().statusCode(expectedStatus);
        logger.info("Status code validated successfully");
    }
}