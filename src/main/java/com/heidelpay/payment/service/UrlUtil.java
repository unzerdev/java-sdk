package com.heidelpay.payment.service;

import java.math.BigDecimal;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
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
import com.heidelpay.payment.exceptions.PropertiesException;
import com.heidelpay.payment.paymenttypes.PaymentType;

public class UrlUtil {
	private static final String PLACEHOLDER_CHARGE_ID = "<chargeId>";

	private static final String PLACEHOLDER_PAYMENT_ID = "<paymentId>";

	private static final String PLACEHOLDER_TYPE_ID = "<typeId>";

	private static final String REFUND_URL = "payments/<paymentId>/charges/<chargeId>/cancels";

	private static final String RECURRING_URL = "types/<typeId>/recurring";

	public static final Logger logger = LogManager.getLogger(UrlUtil.class);

	private final PropertiesUtil properties = new PropertiesUtil();

	private String endPoint;

	public UrlUtil (String endPoint) {
		this.endPoint = endPoint;
	}

	public URL getUrl(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			logger.error("Url '%s' is not valid: %s", url , e.getMessage());
			return null;
		}
	}
	
	public String getRefundUrl(String paymentId, String chargeId) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRestUrl());
		appendSlashIfNeeded(stringBuilder);
		stringBuilder.append(REFUND_URL);
		String result = stringBuilder.toString();
		result = result.replace(PLACEHOLDER_PAYMENT_ID, paymentId);
		return result.replace(PLACEHOLDER_CHARGE_ID, chargeId);
	}
	public String getPaymentUrl(PaymentType paymentType, String paymentId) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRestUrlInternal(paymentType));
		appendSlashIfNeeded(stringBuilder);
		String result = stringBuilder.toString();
		return result.replace(PLACEHOLDER_PAYMENT_ID, paymentId);
	}
	public String getPaymentUrl(PaymentType paymentType, String paymentId, String id) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRestUrlInternal(paymentType));
		appendSlashIfNeeded(stringBuilder);
		stringBuilder.append(id);
		String result = stringBuilder.toString();
		return result.replace(PLACEHOLDER_PAYMENT_ID, paymentId);
	}
	public String getHttpGetUrl(PaymentType paymentType, String id) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRestUrl(paymentType));
		appendSlashIfNeeded(stringBuilder);
		stringBuilder.append(id);
		return  stringBuilder.toString();
	}
	public String getRecurringUrl(Recurring recurring) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRestUrl());
		appendSlashIfNeeded(stringBuilder);
		stringBuilder.append(RECURRING_URL);
		String result = stringBuilder.toString();
		result = result.replace(PLACEHOLDER_TYPE_ID, recurring.getTypeId());
		return result;
	}
	public String getRestUrl(PaymentType paymentType) {
		return getRestUrlInternal(paymentType).replace("<paymentId>/", "");
	}
	private String getRestUrlInternal(PaymentType paymentType) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRestUrl());
		appendSlashIfNeeded(stringBuilder);
		stringBuilder.append(paymentType.getTypeUrl());
		return stringBuilder.toString();
	}
	public String getRestUrl() {
		if(endPoint != null && !endPoint.isEmpty()) {
			return endPoint;
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(properties.getString(PropertiesUtil.REST_ENDPOINT));
			if (stringBuilder.length() == 0) {
				throw new PropertiesException("Properties file heidelpay.properties is empty");
			}
			appendSlashIfNeeded(stringBuilder);
			stringBuilder.append(properties.getString(PropertiesUtil.REST_VERSION));
			appendSlashIfNeeded(stringBuilder);
			return stringBuilder.toString();
		}
	}

	private void appendSlashIfNeeded(StringBuilder stringBuilder) {
		if (stringBuilder.charAt(stringBuilder.length()-1) != '/') {
			stringBuilder.append("/");
		}
	}

	public String getHirePurchaseRateUrl(BigDecimal amount, Currency currency, BigDecimal effectiveInterestRate, Date orderDate) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRestUrl());
		stringBuilder.append("types/hire-purchase-direct-debit/plans?");
		stringBuilder.append("amount=").append(getBigDecimal(amount)).append("&");
		stringBuilder.append("currency=").append(currency.getCurrencyCode()).append("&");
		stringBuilder.append("effectiveInterest=").append(getBigDecimal(effectiveInterestRate)).append("&");
		stringBuilder.append("orderDate=").append(getDate(orderDate));
		return stringBuilder.toString();
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
