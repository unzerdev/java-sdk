package com.unzer.payment.util;

import com.unzer.payment.exceptions.PropertiesException;

import java.io.IOException;
import java.util.Properties;

public class SDKInfo {

    private static final String SDK_VERSION_KEY = "sdk.version";

    private static String version = null;

    private SDKInfo() {
    }

    public static String getVersion() {
        if (version == null) {
            initProperties();
        }
        return version;
    }

    private static void initProperties() {
        try {
            Properties properties = new Properties();
            properties.load(SDKInfo.class.getResourceAsStream("/version.properties"));
            version = properties.getProperty(SDK_VERSION_KEY);
        } catch (IOException e) {
            throw new PropertiesException("Error loading version.properties from Classpath: " + e.getMessage());
        }
    }
}
