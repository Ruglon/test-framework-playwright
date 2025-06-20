package api.tests;


import api.requests.UserListRequest;
import api.steps.UserListSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserListTest extends BaseApiTest {
    private static final Logger logger = LoggerFactory.getLogger(UserListTest.class);
    private UserListSteps userListSteps;

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        userListSteps = new UserListSteps(new UserListRequest());
    }

    @Test(description = "Send request to get list of users",
            groups = {"api", "regression"})
    @Feature("User API")
    @Description("Tests retrieving a list of users")
    public void testListUsers() {
        logger.info("Running testListUsers on thread {}", Thread.currentThread().getName());
        if (userListSteps == null) {
            logger.error("userListSteps is null in testListUsers, reinitializing");
            userListSteps = new UserListSteps(new UserListRequest());
        }
        int page = 2;
        Response response = userListSteps.getUserList(page);
        assertResponse(response, 200, "schemas/get/requests/user-list-schema.json");
    }

    @Test(description = "Send request to get list of users for specific page",
            groups = {"api", "regression"})
    @Feature("User API")
    @Description("Tests retrieving a list of users for a specific page")
    public void testListUsersExactPage() {
        logger.info("Running testListUsersExactPage on thread {}", Thread.currentThread().getName());
        if (userListSteps == null) {
            logger.error("userListSteps is null in testListUsersExactPage, reinitializing");
            userListSteps = new UserListSteps(new UserListRequest());
        }
        Response response = userListSteps.getUserList();
        assertResponse(response, 200, "schemas/get/requests/user-list-schema.json");
    }
}