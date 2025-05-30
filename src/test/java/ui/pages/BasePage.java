package ui.pages;

import com.microsoft.playwright.Locator;
import factory.TestContext;
import ui.helpers.WaitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Base class for all Page Objects. Provides common actions like click, type, wait, etc. */
public abstract class BasePage {
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected final com.microsoft.playwright.Page page;
    protected final WaitHelper waitHelper;

    public BasePage() {
        this.page = TestContext.getPage();
        if (this.page == null) {
            throw new IllegalStateException("Page is null. Ensure TestContext is properly initialized.");
        }
        this.waitHelper = new WaitHelper(page);
    }

    protected void click(String selector) {
        logger.debug("Clicking element: {} on thread {}", selector, Thread.currentThread().getName());
        waitHelper.waitForClickable(selector).click();
    }

    protected void type(String selector, String text) {
        logger.debug("Typing '{}' into element: {} on thread {}", text, selector, Thread.currentThread().getName());
        Locator element = waitHelper.waitForVisible(selector);
        element.clear();
        element.fill(text);
    }

    protected String getText(String selector) {
        logger.debug("Getting text from element: {} on thread {}", selector, Thread.currentThread().getName());
        Locator locator = waitHelper.waitForVisible(selector);
        String text = locator.textContent();
        if (text == null || text.trim().isEmpty()) {
            text = locator.getAttribute("value");
            return text != null ? text.trim() : "";
        }
        return text.trim();
    }

    protected boolean isVisible(String selector) {
        logger.debug("Checking visibility of element: {} on thread {}", selector, Thread.currentThread().getName());
        try {
            return waitHelper.waitForVisible(selector).isVisible();
        } catch (RuntimeException e) {
            return false;
        }
    }

    protected void isVisibleInteractChoice(String selector) {
        logger.debug("Checking accessibility of element: {} on thread {}", selector, Thread.currentThread().getName());
        waitHelper.waitForInteractiveChoice(selector);
    }

    protected void waitForUrlContains(String partialUrl) {
        logger.debug("Waiting for URL to contain: {} on thread {}", partialUrl, Thread.currentThread().getName());
        waitHelper.waitForUrlContains(partialUrl);
    }

    protected void waitForTitleContains(String partialTitle) {
        logger.debug("Waiting for title to contain: {} on thread {}", partialTitle, Thread.currentThread().getName());
        waitHelper.waitForTitleContains(partialTitle);
    }

    protected void selectOption(String selectSelector, String optionValue) {
        logger.debug("Selecting option '{}' in dropdown: {} on thread {}", optionValue, selectSelector, Thread.currentThread().getName());
        Locator selectLocator = waitHelper.waitForVisible(selectSelector);
        selectLocator.selectOption(optionValue);
    }

}