package api.steps;


import api.models.responses.user.list.UserListResponse;
import api.requests.UserListRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines reusable steps for testing the List Users API from reqres.in.
 */
public class UserListSteps {
    private static final Logger logger = LoggerFactory.getLogger(UserListSteps.class);
    private final UserListRequest userListRequest;
    private final ObjectMapper mapper;

    /**
     * Constructs a UserListSteps instance with the provided UserListRequest.
     * @param userListRequest UserListRequest for API calls
     */
    public UserListSteps(UserListRequest userListRequest) {
        this.userListRequest = userListRequest;
        this.mapper = new ObjectMapper();
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

    /**
     * Parses the API response into a UserListResponse object.
     * @param response The API response to parse
     * @return Parsed UserListResponse object
     */
    public UserListResponse parseResponse(Response response) {
        try {
            return mapper.readValue(response.asString(), UserListResponse.class);
        } catch (Exception e) {
            logger.error("Failed to parse response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse response", e);
        }
    }
}