package api.requests;

import config.ApiConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles API requests for listing users from reqres.in.
 */
public class UserListRequest {
    private static final Logger logger = LoggerFactory.getLogger(UserListRequest.class);
    private static final ApiConfigManager config = ApiConfigManager.getInstance();

    /**
     * Constructs a UserListRequest.
     */
    public UserListRequest() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        logger.debug("UserListRequest initialized with BASE_URL: {}", config.get("api.url"));
    }

    /**
     * Sends a GET request to retrieve a list of users for the specified page.
     * @param page Page number for pagination (e.g., 1, 2)
     * @return Response containing the list of users
     */
    public Response listUsers(int page) {
        logger.info("Sending GET request to list users for page: {}", page);
        Response response = RestAssured.given()
                .baseUri(config.get("api.url"))
                .header("X-API-Key", config.get("api.key"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("page", page)
                .when()
                .get("/users");
        logger.debug("Response received: StatusCode={}, Body={}", response.getStatusCode(), response.asString());
        return response;
    }

    /**
     * Sends a GET request to retrieve a list of users without a page parameter.
     * @return Response containing the list of users (default page)
     */
    public Response listUsers() {
        logger.info("Sending GET request to list users without page parameter");
        Response response = RestAssured.given()
                .baseUri(config.get("api.url"))
                .header("X-API-Key", config.get("api.key"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/users");
        logger.debug("Response received: StatusCode={}, Body={}", response.getStatusCode(), response.asString());
        return response;
    }
}