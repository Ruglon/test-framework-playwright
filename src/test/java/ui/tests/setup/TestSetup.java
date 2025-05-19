package ui.tests.setup;

import com.microsoft.playwright.Page;
import config.ConfigManager;
import factory.PlaywrightFactory;
import factory.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSetup {
    private static final Logger logger = LoggerFactory.getLogger(TestSetup.class);

    public void setupBrowserAndNavigate(String browser) {
        String mappedBrowser = BrowserMapper.mapBrowser(browser);
        logger.info("Setting up browser: {} (mapped to {}) for thread {}", browser, mappedBrowser, Thread.currentThread().getName());
        PlaywrightFactory factory = new PlaywrightFactory(
                mappedBrowser,
                ConfigManager.getBoolean("headless", true),
                ConfigManager.getInt("viewport.width", 1920),
                ConfigManager.getInt("viewport.height", 1080)
        );

        TestContext.setFactory(factory);
        TestContext.setBrowserContext(factory.getBrowserContext());
        TestContext.setPage(factory.getPage());

        String url = ConfigManager.get("ui.url");
        if (url == null) {
            throw new IllegalStateException("ui.url is not configured in config.properties");
        }
        TestContext.getPage().navigate(url);
        logger.info("Navigated to URL: {} for thread {}", url, Thread.currentThread().getName());
    }
}