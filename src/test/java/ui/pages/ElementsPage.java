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
    private static final String FULL_NAME_INPUT = "#userNames";
    private static final String EMAIL_INPUT = "#userEmail";
    private static final String CURRENT_ADDRESS_INPUT = "#currentAddress";
    private static final String PERMANENT_ADDRESS_INPUT = "#permanentAddress";
    private static final String SUBMIT_BTN = "#submit";

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
}