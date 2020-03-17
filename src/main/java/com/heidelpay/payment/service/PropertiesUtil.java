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

public class PropertiesUtil {
	public final static Logger logger = LogManager.getLogger(PropertiesUtil.class);
	public final static String REST_ENDPOINT = "rest.endpoint"; 
	public final static String REST_VERSION = "rest.version"; 
	
	private Properties properties;
	
	private void loadProperties() {
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/heidelpay.properties"));
			this.properties = properties;
		} catch (IOException e) {
			logger.error("Error loading heidelpay.properties from Classpath: " + e.getMessage(), e);
			throw new RuntimeException("Error loading heidelpay.properties from Classpath: " + e.getMessage(), e);
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
