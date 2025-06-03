package single;

import com.microsoft.playwright.*;
import org.testng.annotations.*;

public class SingleClass {
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext browserContext;
    private Page page;

    private static final class TestData{
        static final String BASE_URL = "https://demoqa.com/";
    }

    @BeforeSuite
    public void launchBrowser(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @AfterSuite
    public void closeBrowser(){
        playwright.close();
    }

    @BeforeMethod
    public void createContextSetup(){
        browserContext = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080));
        page = browserContext.newPage();
    }

    @AfterMethod
    public void closeContext(){
        browserContext.close();
    }

    @Test
    public void testElementsSingle(){
        page.navigate(TestData.BASE_URL);
        page.locator("xpath=//div[contains(@class, 'top-card') and .//h5[text()='Elements']]").isVisible();
        page.locator("xpath=//div[contains(@class, 'top-card') and .//h5[text()='Elements']]").click();
        page.locator("xpath=//div[@class='header-text' and contains(text(), 'Elements')]").isVisible();
        page.locator("xpath=//div[@class='element-group']//span[text()='Text Box']//parent::li").click();
        page.locator("#userName").fill("Name");
        page.locator("#userEmail").fill("a@a.com");
        page.locator("#currentAddress").fill("Address");
        page.locator("#permanentAddress").fill("Permanent Address");
        page.locator("#submit").click();
    }







}
