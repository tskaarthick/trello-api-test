package com.trello.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static ConfigLoader configLoader;
    private final Properties properties;

    private ConfigLoader() {
        properties = new Properties();
        // Update the path to include the 'configs' directory
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("configs/config.properties");
        try {
            if (inputStream == null) {
                throw new RuntimeException("config.properties file not found in src/test/resources/configs");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getBaseUrl() {
        String prop = properties.getProperty("base.url");
        if (prop != null) return prop;
        else throw new RuntimeException("base.url not specified in the config.properties file.");
    }

    public String getApiKey() {
        String prop = properties.getProperty("api.key");
        if (prop != null) return prop;
        else throw new RuntimeException("api.key not specified in the config.properties file.");
    }

    public String getApiToken() {
        String prop = properties.getProperty("api.token");
        if (prop != null) return prop;
        else throw new RuntimeException("api.token not specified in the config.properties file.");
    }

    public String getOrganizationId() {
        String prop = properties.getProperty("organization.id");
        if (prop != null) return prop;
        else throw new RuntimeException("organization.id not specified in the config.properties file.");
    }
}
