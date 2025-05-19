package api.steps;

import api.requests.UserListRequest;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines reusable steps for testing the List Users API from reqres.in.
 */
public class UserListSteps {
    private static final Logger logger = LoggerFactory.getLogger(UserListSteps.class);
    private final UserListRequest userListRequest;

    /**
     * Constructs a UserListSteps instance with the provided UserListRequest.
     * @param userListRequest UserListRequest for API calls
     */
    public UserListSteps(UserListRequest userListRequest) {
        this.userListRequest = userListRequest;
    }

    /**
     * Retrieves a list of users for the specified page.
     * @param page Page number for pagination (e.g., 1, 2)
     * @return Raw Response from the API
     */
    public Response getUserList(int page) {
        logger.info("Retrieving user list for page: {}", page);
        return userListRequest.listUsers(page);
    }

    /**
     * Retrieves a list of users without a page parameter.
     * @return Raw Response from the API (default page)
     */
    public Response getUserList() {
        logger.info("Retrieving user list without page parameter");
        return userListRequest.listUsers();
    }
}