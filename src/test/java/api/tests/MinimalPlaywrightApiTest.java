package api.tests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MinimalPlaywrightApiTest {
    private static final Logger logger = LoggerFactory.getLogger(MinimalPlaywrightApiTest.class);
    private static final String BASE_URL = "https://reqres.in/api";
    private static final String API_KEY = "reqres-free-v1";
    private Playwright playwright;
    private APIRequestContext request;
    private Map<String, String> headers;

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-API-Key", API_KEY);
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(BASE_URL)
                .setExtraHTTPHeaders(headers));
        logger.info("Setting up API tests with base URI: {}", BASE_URL);
    }

    @AfterClass
    public void teardown() {
        if (request != null) {
            request.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
        logger.info("Tear down API tests");
    }

    @Test
    public void testCreateUser() {
        logger.debug("Starting testCreateUser on thread {}", Thread.currentThread().getName());

        APIResponse response = request.get("/api/users");
        assertResponse("GET", headers, response, 200, "schemas/get/requests/user-list-schema.json");

        logger.debug("List of users gotten successfully API: {}", response.statusText());
    }

    protected void assertResponse(String method, Map<String, String> headers, APIResponse response, int expectedStatus, String schemaPath) {
        logger.info("Asserting response: statusCode={}", expectedStatus);

        if (response.status() != expectedStatus) {
            String body = "";
            try {
                body = response.text();
            } catch (Exception e) {
                body = "Failed to get body: " + e.getMessage();
            }
            throw new AssertionError(String.format(
                    "Request: %s %s, Expected status code %d but was %d, Headers: %s, Response Headers: %s, Body: %s",
                    method, response.url(), expectedStatus, response.status(), headers, response.headers(), body));
        }

        // Schema validation (always required in this test)
        try (InputStream schemaStream = getClass().getClassLoader().getResourceAsStream(schemaPath)) {
            if (schemaStream == null) {
                throw new AssertionError("Schema file not found: " + schemaPath);
            }
            JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
            Schema schema = SchemaLoader.load(rawSchema);
            String responseBody = response.text();
            JSONObject jsonResponse = new JSONObject(new JSONTokener(responseBody));
            schema.validate(jsonResponse);
            logger.debug("JSON schema validation passed for response: {}", responseBody);
        } catch (Exception e) {
            throw new AssertionError("JSON schema validation failed: " + e.getMessage());
        }
    }
}