/*
 * Copyright 2021 Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.service;

import com.unzer.payment.Basket;
import com.unzer.payment.Recurring;
import com.unzer.payment.exceptions.PropertiesException;
import com.unzer.payment.paymenttypes.PaymentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class UrlUtil {
    public static final Logger logger = LogManager.getLogger(UrlUtil.class);
    private static final String PLACEHOLDER_CHARGE_ID = "<chargeId>";
    private static final String PLACEHOLDER_PAYMENT_ID = "<paymentId>";
    private static final String PLACEHOLDER_TYPE_ID = "<typeId>";
    private static final String REFUND_URL = "payments/<paymentId>/charges/<chargeId>/cancels";
    private static final String RECURRING_URL = "types/<typeId>/recurring";
    private final PropertiesUtil properties = new PropertiesUtil();

    private String endPoint;

    public UrlUtil(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * Create URL object from URL string
     *
     * @deprecated This method will be removed from future releases. Use {@link java.net.URL#URL(String)} instead
     */
    @Deprecated
    public URL getUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.error("Url '{}' is not valid: {}", url, e.getMessage());
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
        if(chargeId != null) {
            result = result.replace(PLACEHOLDER_CHARGE_ID, chargeId);
        } else {
            result = result.replace(PLACEHOLDER_CHARGE_ID + "/", "");
        }
        return result;
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
        return stringBuilder.toString();
    }

    public String getHttpGetUrl(String id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getRestUrlWithOutPaymentType());
        appendSlashIfNeeded(stringBuilder);
        stringBuilder.append(id);
        return stringBuilder.toString();
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

    public String getRestUrlWithOutPaymentType() {
        return getRestUrlInternal().replace("<paymentId>/", "");
    }

    @Deprecated
    private String getRestUrlInternal(Basket basket) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(properties.getString(PropertiesUtil.REST_ENDPOINT));
        appendSlashIfNeeded(stringBuilder);

        // v2 is the default Basket resource version
        if (basket.isV2()) {
            stringBuilder.append("v2");
        } else {
            stringBuilder.append("v1");
        }

        appendSlashIfNeeded(stringBuilder);
        stringBuilder.append(basket.getTypeUrl());
        return stringBuilder.toString();
    }

    private String getRestUrlInternal(PaymentType paymentType) {
        // FIXME: remove after Basket v1 is not supported
        if (paymentType instanceof Basket) {
            return getRestUrlInternal((Basket) paymentType);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getRestUrl());
        appendSlashIfNeeded(stringBuilder);
        stringBuilder.append(paymentType.getTypeUrl());
        return stringBuilder.toString();
    }

    private String getRestUrlInternal() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getRestUrl());
        appendSlashIfNeeded(stringBuilder);
        stringBuilder.append("types");
        appendSlashIfNeeded(stringBuilder);
        return stringBuilder.toString();
    }

    public String getRestUrl() {
        if (endPoint != null && !endPoint.isEmpty()) {
            return endPoint;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(properties.getString(PropertiesUtil.REST_ENDPOINT));
            if (stringBuilder.length() == 0) {
                throw new PropertiesException("Properties file unzer.properties is empty");
            }
            appendSlashIfNeeded(stringBuilder);
            stringBuilder.append(properties.getString(PropertiesUtil.REST_VERSION));
            appendSlashIfNeeded(stringBuilder);
            return stringBuilder.toString();
        }
    }

    public String getEndpoint() {
        if (endPoint != null && !endPoint.isEmpty()) {
            return endPoint;
        } else {
            return properties.getString(PropertiesUtil.REST_ENDPOINT);
        }
    }

    private void appendSlashIfNeeded(StringBuilder stringBuilder) {
        if (stringBuilder.charAt(stringBuilder.length() - 1) != '/') {
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
