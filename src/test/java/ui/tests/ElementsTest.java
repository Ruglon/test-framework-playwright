package ui.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.steps.ElementsSteps;
import ui.tests.setup.BaseTest;

public class ElementsTest extends BaseTest {

    @DataProvider(name = "goToElementsData", parallel = true)
    public static Object[][] goToElementsData() {
        return new Object[][] {
                {"chrome"},
                {"firefox"},
                {"safari"}
        };
    }

    @Test(description = "Assert Elements page is available",
            dataProvider = "goToElementsData", groups = {"ui", "elements", "regression"})
    @Feature("Elements Page")
    @Description("Tests navigation to the Elements page for different browsers")
    public void testGoToElementsPage(String browser) {
        setupBrowser(browser);

        ElementsSteps elementsSteps = new ElementsSteps();
        elementsSteps.goToElementsPage();
        Assert.assertTrue(elementsSteps.isOnElementsPage(), "Should be on Elements page");
    }

    @DataProvider(name = "textBoxData", parallel = true)
    public static Object[][] textBoxData() {
        return new Object[][] {
                {"chrome", "Johny Bonny", "a@a.com", "123 Maple Street", "Springfield, IL 62704"},
                {"firefox", "Konny Fex", "f@f.com", "45 Park Lane", "London, W1K 7AF"},
                {"safari", "Sabun Baddan", "e@e.com", "789 Oak Avenue", "Toronto, ON M5V 2H1"}
        };
    }

    @Test(description = "Fill and submit text box form in different browsers",
            dataProvider = "textBoxData", groups = {"ui", "elements", "regression"})
    @Feature("Elements Page")
    @Description("Test text box Elements page for different browsers")
    public void testFillTextBoxInElements(
            String browser, String fullName, String email, String currentAddress, String permanentAddress) {
        setupBrowser(browser);
        String expectedName = "Name:" + fullName;
        String expectedEmail = "Email:" + email;
        String expectedCurrentAddress = "Current Address:" + currentAddress;
        String expectedPermanentAddress = "Permananet Address:" + permanentAddress    ;

        ElementsSteps elementsSteps = new ElementsSteps();
        elementsSteps.goToElementsPage();
        elementsSteps.goToTextBoxScreen();
        elementsSteps.fillTextBoxForm(fullName, email, currentAddress, permanentAddress);
        elementsSteps.clickSubmitTextBox();
        elementsSteps.checkThatNewDataCreated();

        elementsSteps.assertCreatedDataIsCorrect(expectedName, expectedEmail, expectedCurrentAddress, expectedPermanentAddress);
    }

}