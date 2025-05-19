package ui.helpers;

import com.microsoft.playwright.Page;
import factory.TestContext;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class AllureScreenshotListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(AllureScreenshotListener.class);

    private void captureScreenshot(ITestResult result) {
        logger.info("Attempting screenshot for test: {} on thread {}", result.getName(), Thread.currentThread().getName());
        try {
            Page page = TestContext.getPage();
            if (page == null) {
                logger.warn("TestContext.getPage() returned null for test: {}", result.getName());
                return;
            }
            logger.info("Page found for test: {}, URL: {}", result.getName(), page.url());
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment(
                    "Failure Screenshot",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
            logger.info("Screenshot successfully attached for test: {}", result.getName());
        } catch (Exception e) {
            logger.error("Failed to capture or attach screenshot for test {}: {}", result.getName(), e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info("Test failed: {}", result.getName());
        captureScreenshot(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("Test failed within success percentage: {}", result.getName());
        captureScreenshot(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        logger.info("Test failed with timeout: {}", result.getName());
        captureScreenshot(result);
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {}
    @Override
    public void onTestSkipped(ITestResult result) {}
    @Override
    public void onStart(ITestContext context) {}
    @Override
    public void onFinish(ITestContext context) {}
}