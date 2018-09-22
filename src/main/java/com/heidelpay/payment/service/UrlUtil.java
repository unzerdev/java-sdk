package com.heidelpay.payment.service;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.heidelpay.payment.paymenttypes.PaymentType;

public class UrlUtil {
	private static final String PLACEHOLDER_CHARGE_ID = "<chargeId>";

	private static final String PLACEHOLDER_PAYMENT_ID = "<paymentId>";

	private static final String REFUND_URL = "payments/<paymentId>/charges/<chargeId>/cancels";

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
	
	public String getRefundUrl(String paymentId, String chargeId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrl());
		appendSlashIfNeeded(buffer);
		buffer.append(REFUND_URL);
		String result = buffer.toString();
		result = result.replaceAll(PLACEHOLDER_PAYMENT_ID, paymentId);
		return result.replaceAll(PLACEHOLDER_CHARGE_ID, chargeId);
	}
	public String getPaymentUrl(PaymentType paymentType, String paymentId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrlInternal(paymentType));
		appendSlashIfNeeded(buffer);
		String result = buffer.toString();
		return result.replaceAll(PLACEHOLDER_PAYMENT_ID, paymentId);
	}
	public String getPaymentUrl(PaymentType paymentType, String paymentId, String id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrlInternal(paymentType));
		appendSlashIfNeeded(buffer);
		buffer.append(id);
		String result = buffer.toString();
		return result.replaceAll(PLACEHOLDER_PAYMENT_ID, paymentId);
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
