package ui.helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Helper class for Playwright wait operations in page objects. */
public class WaitHelper {
    private static final Logger logger = LoggerFactory.getLogger(WaitHelper.class);
    private final Page page;
    private final double defaultTimeout;

    public WaitHelper(Page page) {
        this.page = page;
        this.defaultTimeout = ConfigManager.getInt("timeout.element", 10000); // 10s default
    }

    public Locator waitForVisible(String selector) {
        logger.debug("Waiting for element to be visible: {} on thread {}", selector, Thread.currentThread().getName());
        try {
            page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(defaultTimeout));
            return page.locator(selector);
        } catch (TimeoutError e) {
            throw new RuntimeException("Element not visible after " + defaultTimeout + "ms: " + selector, e);
        }
    }

    public Locator waitForClickable(String selector) {
        logger.debug("Waiting for element to be clickable: {} on thread {}", selector, Thread.currentThread().getName());
        try {
            page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(defaultTimeout));
            Locator locator = page.locator(selector);
            // Ensure element is enabled
            if (!locator.isEnabled()) {
                throw new RuntimeException("Element is not clickable: " + selector);
            }
            return locator;
        } catch (TimeoutError e) {
            throw new RuntimeException("Element not clickable after " + defaultTimeout + "ms: " + selector, e);
        }
    }

    public void waitForUrlContains(String partialUrl) {
        logger.debug("Waiting for URL to contain: {} on thread {}", partialUrl, Thread.currentThread().getName());
        try {
            page.waitForURL(url -> url.contains(partialUrl), new Page.WaitForURLOptions()
                    .setTimeout(defaultTimeout));
        } catch (TimeoutError e) {
            throw new RuntimeException("URL does not contain '" + partialUrl + "' after " + defaultTimeout + "ms", e);
        }
    }

    public void waitForTitleContains(String partialTitle) {
        logger.debug("Waiting for title to contain: {} on thread {}", partialTitle, Thread.currentThread().getName());
        try {
            page.waitForFunction(
                    "partialTitle => document.title.includes(partialTitle)",
                    partialTitle,
                    new Page.WaitForFunctionOptions().setTimeout(defaultTimeout)
            );
        } catch (TimeoutError e) {
            throw new RuntimeException("Title does not contain '" + partialTitle + "' after " + defaultTimeout + "ms", e);
        }
    }
}