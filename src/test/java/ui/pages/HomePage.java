package ui.pages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private static final String ELEMENTS_BUTTON = "xpath=//div[contains(@class, 'top-card') and .//h5[text()='Elements']]";
    private static final String FORM_BUTTON = "xpath=//div[contains(@class, 'top-card') and .//h5[text()='Forms']]";

    public void clickElementsButton() {
        logger.info("Clicking Elements button on thread {}", Thread.currentThread().getName());
        click(ELEMENTS_BUTTON);
    }

    public void clickFormsButton() {
        logger.info("Clicking Forms button on thread {}", Thread.currentThread().getName());
        click(FORM_BUTTON);
    }
}