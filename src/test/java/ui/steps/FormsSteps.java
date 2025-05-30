package ui.steps;

import ui.pages.FormsPage;
import ui.pages.HomePage;

public class FormsSteps {
    private final HomePage homePage;
    private final FormsPage formsPage;

    public FormsSteps() {
        this.homePage = new HomePage();
        this.formsPage = new FormsPage();
    }

    public void goToFormsPage() {
        homePage.clickFormsButton();

    }

    public void checkIfPracticeFormSelected() {
        formsPage.selectPracticeHeaderIsVisible();
        formsPage.selectPracticeForm();
    }

    public void fillTextForms(String firstName, String secondName, String email){
        formsPage.enterFirstName(firstName);
        formsPage.enterLastName(secondName);
        formsPage.enterEmail(email);
    }

    public void chooseUserGender(String gender){
        formsPage.chooseGender(gender);
    }

    public String chooseDateOfBirth(String day, String month, String year){
        formsPage.selectDateBirth(day, month, year);
        return formsPage.checkChosenDate(day, month, year);
    }




}
