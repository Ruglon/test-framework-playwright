package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();

    static {
        logger.info("Attempting to load test-config.properties from classpath");
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("config.properties not found in classpath");
                throw new IOException("config.properties not found in classpath");
            }
            properties.load(input);
            logger.info("Successfully loaded config.properties");
        } catch (IOException e) {
            logger.error("Failed to load config.properties", e);
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }

        String envVar = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envVar != null && !envVar.isEmpty()) {
            return envVar;
        }

        return properties.getProperty(key, defaultValue);
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}