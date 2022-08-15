package com.unzer.payment.service;

import com.unzer.payment.Basket;
import com.unzer.payment.Recurring;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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

public class UrlUtil {
    private static final String PRODUCTION_ENDPOINT = "https://api.unzer.com";
    private static final String SANDBOX_ENDPOINT = "https://sbx-api.unzer.com";
    private static final String DEVELOPMENT_ENDPOINT = "https://dev-api.unzer.com";
    private static final String STAGING_ENDPOINT = "https://stg-api.unzer.com";
    private static final String DEVELOPMENT_ENVIRONMENT = "DEV";
    private static final String STAGING_ENVIRONMENT = "STG";
    private static final char PRODUCTION_KEY_PREFIX = 'p';
    private static final String PAPI_ENV_NAME = "UNZER_PAPI_ENV";

    private static final String PLACEHOLDER_CHARGE_ID = "<chargeId>";
    private static final String PLACEHOLDER_PAYMENT_ID = "<paymentId>";
    private static final String PLACEHOLDER_TYPE_ID = "<typeId>";
    private static final String REFUND_URL = "payments/<paymentId>/charges/<chargeId>/cancels";
    private static final String RECURRING_URL = "types/<typeId>/recurring";
    private static final String APIVERSION_2 = "v2";
    private static final String APIVERSION_1 = "v1";

    private final String apiEndpoint;

    public UrlUtil(String privateKey) {
        this.apiEndpoint = resolveApiEndpoint(privateKey);
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
        return getRestUrlInternal(paymentType).replace(PLACEHOLDER_PAYMENT_ID + "/", "");
    }

    public String getRestUrlWithOutPaymentType() {
        return getRestUrlInternal().replace(PLACEHOLDER_PAYMENT_ID + "/", "");
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public String getRestUrl() {
        StringBuilder stringBuilder = new StringBuilder(apiEndpoint);
        appendSlashIfNeeded(stringBuilder);
        stringBuilder.append(APIVERSION_1);
        appendSlashIfNeeded(stringBuilder);
        return stringBuilder.toString();
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

    @Deprecated
    private String getRestUrlInternal(Basket basket) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(apiEndpoint);
        appendSlashIfNeeded(stringBuilder);

        // v2 is the default Basket resource version
        if (basket.isV2()) {
            stringBuilder.append(APIVERSION_2);
        } else {
            stringBuilder.append(APIVERSION_1);
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

    private void appendSlashIfNeeded(StringBuilder stringBuilder) {
        if (stringBuilder.charAt(stringBuilder.length() - 1) != '/') {
            stringBuilder.append("/");
        }
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

    /**
     * API endpoint depends on key type and a configured environment.
     * <p/>
     * <table>
     *     <tr><th>Key type</th><th>Configured environment</th><th>Endpoint</th></tr>
     *   <tr>
     *     <td> Production </td> <td> [any] </td> <td>PROD: https://api.unzer.com</td>
     *   </tr>
     *   <tr>
     *     <td> Non-production </td> <td> dev </td> <td>DEV: https://dev-api.unzer.com</td>
     *   </tr>
     *   <tr>
     *     <td> Non-production </td> <td> stg </td> <td>STG: https://stg-api.unzer.com</td>
     *   </tr>
     *   <tr>
     *     <td> Non-production </td> <td> [not dev/stg] </td> <td>SBX: https://sbx-api.unzer.com</td>
     *   </tr>
     * </table>
     *
     * @param privateKey private key to access API
     * @return API endpoint
     */
    private String resolveApiEndpoint(String privateKey) {
        // TODO: add test for resolution

        // Production keys are always routed to production endpoint
        if (privateKey.charAt(0) == PRODUCTION_KEY_PREFIX) {
            return PRODUCTION_ENDPOINT;
        }

        String restEnv = Optional.ofNullable(System.getenv(PAPI_ENV_NAME))
                .orElse("")
                .toUpperCase();

        switch (restEnv) {
            case DEVELOPMENT_ENVIRONMENT:
                return DEVELOPMENT_ENDPOINT;
            case STAGING_ENVIRONMENT:
                return STAGING_ENDPOINT;
            default:
                return SANDBOX_ENDPOINT;
        }
    }
}
