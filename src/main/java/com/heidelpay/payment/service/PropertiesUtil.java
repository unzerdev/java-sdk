package com.heidelpay.payment.service;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.heidelpay.payment.exceptions.PropertiesException;

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
			loadedProperties.load(this.getClass().getResourceAsStream("/heidelpay.properties"));
			this.properties = loadedProperties;

			loadedProperties.load(this.getClass().getResourceAsStream("/test-keys.properties"));
			this.properties.putAll(loadedProperties);
		} catch (IOException e) {
			logger.error("Error loading heidelpay.properties from Classpath: %s", e.getMessage());
			throw new PropertiesException("Error loading heidelpay.properties from Classpath: " + e.getMessage());
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
