package com.unzer.payment.service;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.unzer.payment.exceptions.PropertiesException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    public static final Logger logger = LogManager.getLogger(PropertiesUtil.class);
    public static final String REST_ENDPOINT = "rest.endpoint";
    public static final String REST_VERSION = "rest.version";

    public static final String PUBLIC_KEY1 = "publickey1";
    public static final String PRIVATE_KEY1 = "privatekey1";
    public static final String PRIVATE_KEY2 = "privatekey2";
    public static final String PRIVATE_KEY3 = "privatekey3";
    public static final String MARKETPLACE_PRIVATE_KEY = "marketplacePrivatekey";

    private Properties properties;

    private void loadProperties() {
        try {
            Properties loadedProperties = new Properties();

            loadedProperties.load(this.getClass().getResourceAsStream("/unzer.properties"));
            this.properties = loadedProperties;

            if (System.getProperty(PUBLIC_KEY1) != null) {
                this.properties.put(PUBLIC_KEY1, System.getProperty(PUBLIC_KEY1));
            }
            if (System.getProperty(PRIVATE_KEY1) != null) {
                this.properties.put(PRIVATE_KEY1, System.getProperty(PRIVATE_KEY1));
            }
            if (System.getProperty(PRIVATE_KEY2) != null) {
                this.properties.put(PRIVATE_KEY2, System.getProperty(PRIVATE_KEY2));
            }
            if (System.getProperty(PRIVATE_KEY3) != null) {
                this.properties.put(PRIVATE_KEY3, System.getProperty(PRIVATE_KEY3));
            }
            if (System.getProperty(MARKETPLACE_PRIVATE_KEY) != null) {
                this.properties.put(MARKETPLACE_PRIVATE_KEY, System.getProperty(MARKETPLACE_PRIVATE_KEY));
            }
        } catch (IOException e) {
            throw new PropertiesException("Error loading unzer.properties from Classpath: " + e.getMessage());
        }
    }

    public String getString(String key) {
        return getProperties().getProperty(key);
    }

    private Properties getProperties() {
        if (this.properties == null) loadProperties();
        return properties;
    }
}
