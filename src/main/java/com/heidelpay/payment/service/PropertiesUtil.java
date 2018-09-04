package com.heidelpay.payment.service;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	public final static Logger logger = Logger.getLogger(PropertiesUtil.class);
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
