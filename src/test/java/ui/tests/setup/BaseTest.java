package ui.tests.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private final TestSetup testSetup = new TestSetup();
    private final TestTeardown testTeardown = new TestTeardown();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Starting setup for thread {}", Thread.currentThread().getName());
    }

    protected void setupBrowser(String browser) {
        logger.info("Setting up browser {} for thread {}", browser, Thread.currentThread().getName());
        testSetup.setupBrowserAndNavigate(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        testTeardown.tearDown(result);
    }
}