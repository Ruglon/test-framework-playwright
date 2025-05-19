package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manages API-specific configurations loaded from config.properties.
 * Provides methods to access properties with type-safe retrieval and defaults.
 * Uses singleton pattern to ensure a single instance.
 */
public class ApiConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ApiConfigManager.class);
    private static final String CONFIG_FILE = "config.properties";
    private static ApiConfigManager instance;
    private final Properties properties;

    // Private constructor to prevent instantiation
    private ApiConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Returns the singleton instance of ApiConfigManager.
     * @return ApiConfigManager instance
     */
    public static synchronized ApiConfigManager getInstance() {
        if (instance == null) {
            instance = new ApiConfigManager();
        }
        return instance;
    }

    // Loads properties from config.properties
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.error("Unable to find {}", CONFIG_FILE);
                throw new IOException("Configuration file " + CONFIG_FILE + " not found");
            }
            properties.load(input);
            logger.info("Loaded API configuration from {}", CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Failed to load {}: {}", CONFIG_FILE, e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    /**
     * Retrieves a property value by key, returning null if not found.
     * @param key Property key
     * @return Property value or null
     */
    public String get(String key) {
        return get(key, null);
    }

    /**
     * Retrieves a property value by key, with an optional default.
     * @param key Property key (e.g., "api.base.url")
     * @param defaultValue Value to return if key is not found
     * @return Property value or default
     */
    public String get(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Retrieved property: {} = {}", key, value);
        return value;
    }

    /**
     * Retrieves an integer property by key, with a default if invalid or not found.
     * @param key Property key (e.g., "api.timeout")
     * @param defaultValue Default integer value
     * @return Integer value or default
     */
    public int getInt(String key, int defaultValue) {
        String value = get(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer for key {}: {}, using default {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Retrieves a boolean property by key, with a default if not found.
     * @param key Property key
     * @param defaultValue Default boolean value
     * @return Boolean value or default
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }
}