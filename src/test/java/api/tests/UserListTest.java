package api.tests;


import api.models.responses.user.list.UserListResponse;
import api.requests.UserListRequest;
import api.steps.UserListSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserListTest extends BaseApiTest {
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
        int page = 2;
        Response response = userListSteps.getUserList(page);
        assertResponse(response, 200, "api/models/schemas/get/requests/user-list-schema.json");
//        Assert.assertEquals(response.getPage(), page, "Page number should match requested page");
//        Assert.assertTrue(response.getPerPage() > 0, "Per page should be greater than 0");
//        Assert.assertTrue(response.getTotal() > 0, "Total users should be greater than 0");
//        Assert.assertFalse(response.getData().isEmpty(), "User list should not be empty");
//        Assert.assertNotNull(response.getSupport(), "Support information should be present");
    }

    @Test(description = "Send request to get list of users for specific page",
            groups = {"api", "regression"})
    @Feature("User API")
    @Description("Tests retrieving a list of users for a specific page")
    public void testListUsersExactPage() {
        Response response = userListSteps.getUserList();
        assertResponse((Response) response, 200, "schemas/user-list-schema.json");
//        Assert.assertTrue(response.getPerPage() > 0, "Per page should be greater than 0");
//        Assert.assertTrue(response.getTotal() > 0, "Total users should be greater than 0");
//        Assert.assertFalse(response.getData().isEmpty(), "User list should not be empty");
//        Assert.assertNotNull(response.getSupport(), "Support information should be present");
    }
}