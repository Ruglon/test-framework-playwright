package ui.tests.setup;

import factory.PlaywrightFactory;
import factory.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

public class TestTeardown {
    private static final Logger logger = LoggerFactory.getLogger(TestTeardown.class);

    public void tearDown(ITestResult result) {
        logger.info("Tearing down for thread {}", Thread.currentThread().getName());
        try {
            PlaywrightFactory factory = TestContext.getFactory();
            if (factory != null) {
                if (result.getStatus() == ITestResult.SUCCESS || result.getStatus() == ITestResult.SKIP) {
                    factory.close();
                } else {
                    logger.info("Preserving Page for screenshot on test failure for thread {}", Thread.currentThread().getName());
                }
            }
        } catch (Exception e) {
            logger.error("Error during teardown for thread {}: {}", Thread.currentThread().getName(), e.getMessage());
        } finally {
            TestContext.removeFactory();
            TestContext.remove();
        }
    }
}
