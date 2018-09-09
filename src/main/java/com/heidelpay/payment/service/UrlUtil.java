package com.heidelpay.payment.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.heidelpay.payment.paymenttypes.PaymentType;

public class UrlUtil {
	public final static Logger logger = Logger.getLogger(UrlUtil.class);

	private PropertiesUtil properties = new PropertiesUtil();

	public URL getUrl(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			logger.error("Url '" + url + "' is not valid: " + e.getMessage());
			return null;
		}
	}
	
	public String getPaymentUrl(PaymentType paymentType, String paymentId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrlInternal(paymentType));
		appendSlashIfNeeded(buffer);
		String result = buffer.toString();
		return result.replaceAll("<paymentId>", paymentId);
	}
	public String getPaymentUrl(PaymentType paymentType, String paymentId, String id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrlInternal(paymentType));
		appendSlashIfNeeded(buffer);
		buffer.append(id);
		String result = buffer.toString();
		return result.replaceAll("<paymentId>", paymentId);
	}
	public String getHttpGetUrl(PaymentType paymentType, String id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrl(paymentType));
		appendSlashIfNeeded(buffer);
		buffer.append(id);
		return  buffer.toString();
	}
	public String getRestUrl(PaymentType paymentType) {
		return getRestUrlInternal(paymentType).replaceAll("<paymentId>/", "");
	}
	private String getRestUrlInternal(PaymentType paymentType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrl());
		appendSlashIfNeeded(buffer);
		buffer.append(paymentType.getTypeUrl());
		return buffer.toString();
	}
	public String getRestUrl() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(properties.getString(PropertiesUtil.REST_ENDPOINT));
		if (buffer.length() == 0) {
			throw new RuntimeException("Properties file heidelpay.properties is empty");
		}
		appendSlashIfNeeded(buffer);
		buffer.append(properties.getString(PropertiesUtil.REST_VERSION));
		appendSlashIfNeeded(buffer);
		return buffer.toString();
	}

	private void appendSlashIfNeeded(StringBuffer buffer) {
		if (buffer.charAt(buffer.length()-1) != '/') {
			buffer.append("/");
		}
	}

}
