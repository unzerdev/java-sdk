package com.unzer.payment.util;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

public class SDKInfo {

	private static final String SDK_VERSION_KEY = "sdk.version";
	
	private static String version = null;
	
	private SDKInfo() {}
	
	public static String getVersion() {
		if(version == null) {
			initProperties();
		}
		return version;
	}

	private static void initProperties() {
		try {
			Properties properties = new Properties();
			properties.load(SDKInfo.class.getResourceAsStream("/version.properties"));
			version  = properties.getProperty(SDK_VERSION_KEY);
		} catch (IOException e) {
			throw new PropertiesException("Error loading version.properties from Classpath: " + e.getMessage());
		}
	}
}
