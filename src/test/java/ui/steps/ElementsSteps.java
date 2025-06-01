package ui.steps;

import ui.pages.ElementsPage;
import ui.pages.HomePage;

public class ElementsSteps {
    private final HomePage homePage;
    private final ElementsPage elementsPage;

    public ElementsSteps() {
        this.homePage = new HomePage();
        this.elementsPage = new ElementsPage();
    }

    public void goToElementsPage() {
        homePage.clickElementsButton();
    }

    public void goToTextBoxScreen() {
        elementsPage.clickTextBoxBtn();
    }

    public void fillTextBoxForm(String fullName, String email, String currentAddress, String permanentAddress) {
        elementsPage.enterFullName(fullName);
        elementsPage.enterEmail(email);
        elementsPage.enterCurrentAddress(currentAddress);
        elementsPage.enterPermanentAddress(permanentAddress);
    }

    public void clickSubmitTextBox() {
        elementsPage.clickTextBoxSubmitBtn();
    }

    public boolean isOnElementsPage() {
        return elementsPage.userOnElementsPage();
    }

    public void checkThatNewDataCreated() {
        elementsPage.waitForSubmitDataDisplayed();
    }

    public void assertCreatedDataIsCorrect(String name, String email, String currentAddress, String permanentAddress) {
        String actualName = elementsPage.getCreatedElementsName();
        if (!name.equals(actualName)) {
            throw new AssertionError(String.format("%s expected, But was %s", name, actualName));
        }
        String actualEmail = elementsPage.getCreatedElementsEmail();
        if (!email.equals(actualEmail)) {
            throw new AssertionError(String.format("%s expected, But was %s", email, actualEmail));
        }
        String actualCurrentAddress = elementsPage.getCreatedElementsCurrentAddress();
        if (!currentAddress.equals(actualCurrentAddress)) {
            throw new AssertionError(String.format("%s expected, But was %s", currentAddress, actualCurrentAddress));
        }
        String actualPermanentAddress = elementsPage.getCreatedElementsPermanentAddress();
        if (!permanentAddress.equals(actualPermanentAddress)) {
            throw new AssertionError(String.format("%s expected, But was %s", permanentAddress, actualPermanentAddress));
        }
    }
}