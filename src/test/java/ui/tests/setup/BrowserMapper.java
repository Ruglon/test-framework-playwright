package ui.tests.setup;

import config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserMapper {

    private static final Logger logger = LoggerFactory.getLogger(BrowserMapper.class);

    public static String mapBrowser(String browser) {
        String mappedBrowser = ConfigManager.get("browser." + browser.toLowerCase());
        if (mappedBrowser == null) {
            logger.error("Unsupported browser: {}", browser);
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        logger.info("Mapped browser: {} to {}", browser, mappedBrowser);
        return mappedBrowser;
    }
}
