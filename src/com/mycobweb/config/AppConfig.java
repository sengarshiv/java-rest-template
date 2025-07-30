// config/AppConfig.java
package com.mycobweb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();

    // Load properties file at startup
    static {
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new IllegalStateException("⚠️ Could not find application.properties");
            }

            props.load(input);
            System.out.println("✅ Loaded configuration from application.properties");

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // === Server Config ===
    public static int SERVER_PORT() {
        return getInt("server.port", 8086);
    }

    // === Logging Config ===
    public static boolean LOGGING_ENABLED() {
        return getBoolean("logging.enabled", true);
    }

    public static boolean LOG_TO_CONSOLE() {
        return getBoolean("logging.to.console", true);
    }

    public static boolean LOG_TO_FILE() {
        return getBoolean("logging.to.file", true);
    }

    public static String LOG_FILE_NAME() {
        return props.getProperty("logging.file.name", "logs/app.log");
    }

    // Helper: Get int with default
    private static int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid int value for " + key + ": " + value + ", using default: " + defaultValue);
            return defaultValue;
        }
    }

    // Helper: Get boolean with default
    private static boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    // Prevent instantiation
    private AppConfig() {}
}