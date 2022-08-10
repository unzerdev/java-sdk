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

import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final String API_ENVIRONMENT = "unzer.environment";
    private static final String PRODUCTION_ENDPOINT = "https://api.unzer.com";
    private static final String DEVELOPMENT_ENDPOINT = "https://dev-api.unzer.com";
    private static final String STAGING_ENDPOINT = "https://stg-api.unzer.com";
    private static final String SANDBOX_ENDPOINT = "https://sbx-api.unzer.com";
    private static final String DEVELOPMENT_ENVIRONMENT = "DEV";
    private static final String STAGING_ENVIRONMENT = "STG";
    private static final char PRODUCTION_KEY_PREFIX = 'p';

    private static final Properties properties;
    static {
        try {
            Properties loadedProperties = new Properties();
            loadedProperties.load(Configuration.class.getResourceAsStream("/unzer.properties"));
            properties = loadedProperties;
        } catch (IOException e) {
            throw new PropertiesException("Error loading unzer.properties from Classpath: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * API endpoint depends on key type and a configured environment.
     * <p/>
     * <table>
     *     <tr><th>Key type</th><th>Configured environment</th><th>Endpoint</th></tr>
     *   <tr>
     *     <td> Production </td> <td> [any] </td> <td>PROD: https://api.unzer.com</td>
     *   </tr>
     *   <tr>
     *     <td> Non-production </td> <td> dev </td> <td>DEV: https://dev-api.unzer.com</td>
     *   </tr>
     *   <tr>
     *     <td> Non-production </td> <td> stg </td> <td>STG: https://stg-api.unzer.com</td>
     *   </tr>
     *   <tr>
     *     <td> Non-production </td> <td> [not dev/stg] </td> <td>SBX: https://sbx-api.unzer.com</td>
     *   </tr>
     * </table>
     *
     * @param privateKey private key to access API
     * @return API endpoint
     */
    public static String resolveApiEndpoint(String privateKey) {
        // Production keys are always routed to production endpoint
        if (privateKey.charAt(0) == PRODUCTION_KEY_PREFIX) {
            return PRODUCTION_ENDPOINT;
        }

        String restEnv = properties.getProperty(API_ENVIRONMENT).toUpperCase();
        switch (restEnv) {
            case DEVELOPMENT_ENVIRONMENT:
                return DEVELOPMENT_ENDPOINT;
            case STAGING_ENVIRONMENT:
                return STAGING_ENDPOINT;
            default:
                return SANDBOX_ENDPOINT;
        }
    }
}
