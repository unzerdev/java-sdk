package com.heidelpay.payment.service;

import com.heidelpay.payment.paymenttypes.PaymentType;

public class UrlUtil {
	private PropertiesUtil properties = new PropertiesUtil();

	public String getRestUrl(PaymentType paymentType) {
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
