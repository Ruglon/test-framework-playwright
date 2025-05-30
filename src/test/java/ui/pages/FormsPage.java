package ui.pages;

import com.microsoft.playwright.Locator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormsPage extends BasePage{
    private static final Logger logger = LoggerFactory.getLogger(FormsPage.class);


    private static final String PRACTICE_FORM_HEADER = "xpath=//h1[text()='Practice Form']";
    private static final String PRACTICE_FORM = "xpath=//ul[@class='menu-list']//span[text()='Practice Form']//parent::li";
    private static final String FORM_FIRST_NAME = "xpath=//input[@placeholder='First Name']";
    private static final String FORM_LAST_NAME = "xpath=//input[@placeholder='Last Name']";
    private static final String FORM_EMAIL = "#userEmail";
    private static final String FORM_GENDER_MALE = "xpath=//input[@value='Male']//parent::div";
    private static final String FORM_GENDER_FEMALE = "xpath=//input[@value='Female']//parent::div";
    private static final String FORM_GENDER_OTHER = "xpath=///input[@value='Other']//parent::div";
    private static final String FORM_MOBILE = "#userNumber";
    private static final String FORM_DATE_BIRTH = "#dateOfBirthInput";
    private static final String FORM_DATE_SUBJECT = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String FORM_HOBBIES_SPORT = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String FORM_HOBBIES_READING = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String FORM_HOBBIES_MUSIC = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String FORM_UPLOAD_PICTURE = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String FORM_CURRENT_ADDRESS = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String FORM_SELECT_CITY = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String FORM_SELECT_STATE = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";

    // Date picker
    private static final String MONTH_DROPDOWN = "xpath=//select[contains(@class, 'month-select')]";
    private static final String MONTH_OPTION = "xpath=//select[contains(@class, 'month-select')]//option[text()='%s']";
    private static final String YEAR_DROPDOWN = "xpath=//select[contains(@class, 'year-select')]";
    private static final String YEAR_OPTION = "xpath=//select[contains(@class, 'year-select')]//option[text()='%s']";
    private static final String DAY_OPTION = "xpath=//div[contains(@class, 'react-datepicker__day') and text()='%s' and not(contains(@class, 'outside-month'))]";




    public void selectPracticeForm(){
        Locator practiceForm = page.locator(PRACTICE_FORM);
        Locator practiceFormHeader = page.locator(PRACTICE_FORM_HEADER);
        if(!practiceFormHeader.isVisible()){
            practiceForm.click();
        }
    }

    public void selectPracticeHeaderIsVisible() {
        isVisible(PRACTICE_FORM_HEADER);
    }

    public void selectDateBirth(String day, String month, String year) {
        logger.info("Clicking on Date birth field{}", Thread.currentThread().getName());
        page.locator(FORM_DATE_BIRTH).click();
        logger.info("Waiting for month field to show{}", Thread.currentThread().getName());

        page.locator(MONTH_DROPDOWN).isVisible();
        page.locator(MONTH_DROPDOWN).selectOption(month);
        selectOption(YEAR_DROPDOWN, year);
        click(String.format(DAY_OPTION, day));
    }

    public String checkChosenDate(String day, String month, String year){
        return getText(FORM_DATE_BIRTH).replace(" ", "");

    }

    public void enterFirstName(String firstName){
        type(FORM_FIRST_NAME, firstName);
    }

    public void enterLastName(String lastName){
        type(FORM_LAST_NAME, lastName);
    }

    public void enterEmail(String email){
        type(FORM_EMAIL, email);
    }

    public void chooseGender(String gender){
        switch (gender != null ? gender : "") {
            case "Male" -> page.locator(FORM_GENDER_MALE).click();
            case "Female" -> page.locator(FORM_GENDER_FEMALE).click();
            case "Other" -> page.locator(FORM_GENDER_OTHER).click();
            default -> throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }



}
