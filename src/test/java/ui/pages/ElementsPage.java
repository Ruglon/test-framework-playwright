package ui.pages;

public class ElementsPage extends BasePage {
    // Locators
    private static final String ELEMENTS_HEADER = "xpath=//div[@class='header-text' and contains(text(), 'Elements')]";
    private static final String TEXT_BOX_BTN = "xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li";
    private static final String CHECK_BOX_BTN = "xpath=//div[@class='element-group']//span[text()='Check Box']//parent::li";
    private static final String RADIO_BTNS = "xpath=//div[@class='element-group']//span[text()='Radio Button']//parent::li";
    private static final String WEB_TABLES_BTN = "xpath=//div[@class='element-group']//span[text()='Web Tables']//parent::li";
    private static final String BUTTONS_BTN = "xpath=//div[@class='element-group']//span[text()='Buttons']//parent::li";
    private static final String LINKS_BTN = "xpath=//div[@class='element-group']//span[text()='Links']//parent::li";
    private static final String BROKEN_LINKS_BTN = "xpath=//div[@class='element-group']//span[text()='Broken Links - Images']//parent::li";
    private static final String UPLOAD_DOWNLOAD_BTN = "xpath=//div[@class='element-group']//span[text()='Upload and Download']//parent::li";
    private static final String DYNAMIC_PROPERTIES_BTN = "xpath=//div[@class='element-group']//span[text()='Dynamic Properties']//parent::li";

    // Text box
    private static final String FULL_NAME_INPUT = "#userName";
    private static final String EMAIL_INPUT = "#userEmail";
    private static final String CURRENT_ADDRESS_INPUT = "#currentAddress";
    private static final String PERMANENT_ADDRESS_INPUT = "#permanentAddress";
    private static final String SUBMIT_BTN = "#submit";
    private static final String CREATED_DATA = "#output";
    private static final String CREATED_ELEMENTS_NAME = "xpath=//div[@id='output']//p[@id='name']";
    private static final String CREATED_ELEMENTS_EMAIL = "xpath=//div[@id='output']//p[@id='email']";
    private static final String CREATED_ELEMENTS_CURRENT_ADDRESS = "xpath=//div[@id='output']//p[@id='currentAddress']";
    private static final String CREATED_ELEMENTS_PERMANENT_ADDRESS = "xpath=//div[@id='output']//p[@id='permanentAddress']";


    // Check box
    private static final String PAGE_TITLE = "h3";

    // Actions
    public boolean userOnElementsPage() {
        return isVisible(ELEMENTS_HEADER);
    }

    // Text box actions
    public void clickTextBoxBtn() {
        click(TEXT_BOX_BTN);
    }

    public void enterFullName(String fullName) {
        type(FULL_NAME_INPUT, fullName);
    }

    public void enterEmail(String email) {
        type(EMAIL_INPUT, email);
    }

    public void enterCurrentAddress(String address) {
        type(CURRENT_ADDRESS_INPUT, address);
    }

    public void enterPermanentAddress(String address) {
        type(PERMANENT_ADDRESS_INPUT, address);
    }

    public void clickTextBoxSubmitBtn() {
        click(SUBMIT_BTN);
    }
    public void waitForSubmitDataDisplayed() {
        isVisible(CREATED_DATA);
    }

    public String getCreatedElementsName() {
        return getText(CREATED_ELEMENTS_NAME);
    }

    public String getCreatedElementsEmail() {
        return getText(CREATED_ELEMENTS_EMAIL);
    }

    public String getCreatedElementsCurrentAddress() {
        return getText(CREATED_ELEMENTS_CURRENT_ADDRESS);
    }

    public String getCreatedElementsPermanentAddress() {
        return getText(CREATED_ELEMENTS_PERMANENT_ADDRESS);
    }
}