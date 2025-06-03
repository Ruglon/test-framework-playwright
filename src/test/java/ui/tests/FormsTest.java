package ui.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.steps.FormsSteps;
import ui.tests.setup.BaseTest;

import static org.testng.Assert.assertEquals;

public class FormsTest extends BaseTest {


    @DataProvider(name = "formTextData", parallel = true)
    public static Object[][] formTextData(){
        return new Object[][]{
                {"chrome", "Johny", "Bonny", "a@a.com", "Male", "14", "May", "1990"},
                {"firefox", "Konny", "Fex", "f@f.com", "Female", "21", "November", "2002"},
                {"safari", "Sabun", "Baddan", "e@e.com", "Other", "7", "August", "2006"}
        };
    }

    @Test(description = "Fill and submit Practice Form", dataProvider = "formTextData",
    groups = {"ui", "regression"})
    @Feature("Forms Page")
    @Description("Test of Forms page for different browsers")
    public void testFillPracticeForm(String browser, String firstName, String lastName, String email,
                                     String gender, String day, String month, String year){
        setupBrowser(browser);
        String shorMonth = month.length() <= 3 ? month : month.substring(0, 3);

        FormsSteps formsSteps = new FormsSteps();
        formsSteps.goToFormsPage();
        formsSteps.checkIfPracticeFormSelected();
        formsSteps.fillTextForms(firstName, lastName, email);
        formsSteps.chooseUserGender(gender);
        assertEquals(formsSteps.chooseDateOfBirth(day, month, year), String.format("%s%s%s", day, shorMonth, year));
    }






}
