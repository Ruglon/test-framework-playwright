package api.requests;

import api.client.RestAssuredClient;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles API requests for listing users from reqres.in.
 */
public class UserListRequest {
    private static final Logger logger = LoggerFactory.getLogger(UserListRequest.class);
    private final RestAssuredClient client;

    /**
     * Constructs a UserListRequest.
     */
    public UserListRequest() {
        this.client = new RestAssuredClient();
        logger.debug("UserListRequest initialized with client: {}", client);
    }

    /**
     * Sends a GET request to retrieve a list of users for the specified page.
     * @param page Page number for pagination (e.g., 1, 2)
     * @return Response containing the list of users
     */
    public Response listUsers(int page) {
        logger.info("Sending GET request to list users for page: {}", page);
        RequestSpecification spec = client.createRequestSpec();
        logger.debug("Using RequestSpecification: {}", spec);
        Response response = spec
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
        RequestSpecification spec = client.createRequestSpec();
        logger.debug("Using RequestSpecification: {}", spec);
        Response response = spec
                .when()
                .get("/users");
        logger.debug("Response received: StatusCode={}, Body={}", response.getStatusCode(), response.asString());
        return response;
    }
}