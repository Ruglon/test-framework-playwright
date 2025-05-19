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
}