package factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaywrightFactory {
    private static final Logger logger = LoggerFactory.getLogger(PlaywrightFactory.class);
    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext context;
    private final Page page;

    public PlaywrightFactory(String browserName, boolean headless, int width, int height) {
        logger.info("Initializing browser: {} (headless: {}) for thread {}", browserName, headless, Thread.currentThread().getName());
        playwright = Playwright.create();

        BrowserType browserType = switch (browserName.toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };

        browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless));
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(width, height));
        page = context.newPage();
        logger.info("Browser initialized successfully for thread {}", Thread.currentThread().getName());
    }

    public Page getPage() {
        return page;
    }

    public BrowserContext getBrowserContext() {
        return context;
    }

    public void close() {
        if (page != null) {
            page.close();
            logger.info("Page closed for thread {}", Thread.currentThread().getName());
        }
        if (context != null) {
            context.close();
            logger.info("Browser context closed for thread {}", Thread.currentThread().getName());
        }
        if (browser != null) {
            browser.close();
            logger.info("Browser closed for thread {}", Thread.currentThread().getName());
        }
        if (playwright != null) {
            playwright.close();
            logger.info("Playwright closed for thread {}", Thread.currentThread().getName());
        }
    }
}