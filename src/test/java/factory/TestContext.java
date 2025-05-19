package factory;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestContext {
    private static final Logger logger = LoggerFactory.getLogger(TestContext.class);
    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();
    private static final ThreadLocal<PlaywrightFactory> factory = new ThreadLocal<>();

    public static void setBrowserContext(BrowserContext context) {
        browserContext.set(context);
        logger.info("BrowserContext set for thread {}", Thread.currentThread().getName());
    }

    public static BrowserContext getBrowserContext() {
        BrowserContext context = browserContext.get();
        logger.info("Retrieved BrowserContext for thread {}", Thread.currentThread().getName());
        return context;
    }

    public static void setPage(Page pg) {
        page.set(pg);
        logger.info("Page set for thread {}", Thread.currentThread().getName());
    }

    public static Page getPage() {
        Page pg = page.get();
        logger.info("Retrieved Page for thread {}", Thread.currentThread().getName());
        return pg;
    }

    public static void setFactory(PlaywrightFactory f) {
        factory.set(f);
        logger.info("PlaywrightFactory set for thread {}", Thread.currentThread().getName());
    }

    public static PlaywrightFactory getFactory() {
        PlaywrightFactory f = factory.get();
        logger.info("Retrieved PlaywrightFactory for thread {}", Thread.currentThread().getName());
        return f;
    }

    public static void removeFactory() {
        factory.remove();
        logger.info("PlaywrightFactory removed for thread {}", Thread.currentThread().getName());
    }

    public static void remove() {
        browserContext.remove();
        page.remove();
        logger.info("TestContext cleared for thread {}", Thread.currentThread().getName());
    }
}