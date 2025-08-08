package com.telerik.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private final Properties properties;
    private static ConfigReader configReader = null;

    // Private constructor to prevent external instantiation
    private ConfigReader() {
        properties = new Properties();

        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("configurations/config.properties")) {

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties in configurations folder.");
                return;
            }

            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Public static method to return the singleton instance
    public static ConfigReader getConfigReader() {
        if (configReader == null) {
            configReader = new ConfigReader();
        }
        return configReader;
    }

    // Public methods to access properties
    public String getUrL() {
        return properties.getProperty("URL");
    }

    public String getBrowser() {
        return properties.getProperty("BROWSER");
    }

    public long getGlobalWaitValue() {
        return Long.parseLong(properties.getProperty("GLOBAL_WAIT"));
    }

    public String getTestDataWorkbook() {
        return properties.getProperty("TEST_DATA_WORKBOOK");
    }

    public String getMaxRetriesFailCaseCount() {
        return properties.getProperty("MAX_RETRIES_FAIL_CASE");
    }

    public String sentMailToUser() {
        return properties.getProperty("MAIL_REPORT");
    }
    public String getRunMode() {
        return properties.getProperty("RUN_MODE");
    }
    public String getHubURL() {
        return properties.getProperty("GRID_HUB_URL");
    }
}
