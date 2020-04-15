package com.heidelpay.payment.service;

import java.math.BigDecimal;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.paymenttypes.PaymentType;

public class UrlUtil {
	private static final String PLACEHOLDER_CHARGE_ID = "<chargeId>";

	private static final String PLACEHOLDER_PAYMENT_ID = "<paymentId>";

	private static final String PLACEHOLDER_TYPE_ID = "<typeId>";

	private static final String REFUND_URL = "payments/<paymentId>/charges/<chargeId>/cancels";

	private static final String RECURRING_URL = "types/<typeId>/recurring";

	public final static Logger logger = LogManager.getLogger(UrlUtil.class);

	private PropertiesUtil properties = new PropertiesUtil();

	private String endPoint;

	public UrlUtil (String endPoint) {
		this.endPoint = endPoint;
	}

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
	public String getRecurringUrl(Recurring recurring) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrl());
		appendSlashIfNeeded(buffer);
		buffer.append(RECURRING_URL);
		String result = buffer.toString();
		result = result.replaceAll(PLACEHOLDER_TYPE_ID, recurring.getType());
		return result;
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
		if(endPoint != null && !endPoint.isEmpty()) {
			return endPoint;
		} else {
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
	}

	private void appendSlashIfNeeded(StringBuffer buffer) {
		if (buffer.charAt(buffer.length()-1) != '/') {
			buffer.append("/");
		}
	}

	public String getHirePurchaseRateUrl(BigDecimal amount, Currency currency, BigDecimal effectiveInterestRate, Date orderDate) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRestUrl());
		buffer.append("types/hire-purchase-direct-debit/plans?");
		buffer.append("amount=").append(getBigDecimal(amount)).append("&");
		buffer.append("currency=").append(currency.getCurrencyCode()).append("&");
		buffer.append("effectiveInterest=").append(getBigDecimal(effectiveInterestRate)).append("&");
		buffer.append("orderDate=").append(getDate(orderDate));
		return buffer.toString();
	}

	private String getDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	private String getBigDecimal(BigDecimal decimal) {
		NumberFormat df = NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(0);
		df.setGroupingUsed(false);
		return df.format(decimal);
	}

}
