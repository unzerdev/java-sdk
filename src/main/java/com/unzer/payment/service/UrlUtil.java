package com.unzer.payment.service;

import com.unzer.payment.Resource;
import com.unzer.payment.communication.UnzerHttpRequest;
import com.unzer.payment.communication.api.ApiConfig;
import com.unzer.payment.communication.api.ApiConfigs;
import com.unzer.payment.models.PaylaterInvoiceConfigRequest;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class UrlUtil {
    private final String apiEndpoint;

    public UrlUtil(String privateKey) {
        this.apiEndpoint = resolveApiEndpoint(privateKey);
    }

    public UrlUtil(String privateKey, ApiConfig apiConfig) {
        this.apiEndpoint = resolveApiEndpoint(privateKey, apiConfig);
    }

    private static String resolveApiEndpoint(String privateKey, ApiConfig apiConfig) {
        // 'p-' for production, 's-' for sandbox
        return privateKey.charAt(0) == 'p'
                ? apiConfig.getBaseUrl()
                : apiConfig.getTestBaseUrl();
    }

    private static String resolveApiEndpoint(String privateKey) {
        return resolveApiEndpoint(privateKey, ApiConfigs.PAYMENT_API);
    }

    public String getRestUrl() {
        return apiEndpoint + "/v1/";
    }

    public String getUrl(Resource resource) {
        return apiEndpoint + resource.getUrl();
    }

    public String getUrl(Resource resource, UnzerHttpRequest.UnzerHttpMethod httpMethod) {
        return httpMethod == UnzerHttpRequest.UnzerHttpMethod.POST
                ? apiEndpoint + resource.getUrl()
                : apiEndpoint + resource.getUrl() + "/" + resource.getId();
    }

    public String getHirePurchaseRateUrl(
            BigDecimal amount,
            Currency currency,
            BigDecimal effectiveInterestRate,
            Date orderDate
    ) {
        return getRestUrl()
                + "types/hire-purchase-direct-debit/plans?amount="
                + Format.bigDecimal(amount)
                + "&currency="
                + currency.getCurrencyCode()
                + "&effectiveInterest="
                + Format.bigDecimal(effectiveInterestRate)
                + "&orderDate="
                + Format.date(orderDate);
    }

    public String getPaymentTypeConfigUrl(PaylaterInvoiceConfigRequest configRequest) {
        return apiEndpoint + configRequest.getRequestUrl();
    }

    public String getInstallmentPlanUrl(InstallmentPlansRequest installmentPlansRequest) {
        return apiEndpoint + installmentPlansRequest.getUrl();
    }

    private static class Format {
        public static String bigDecimal(BigDecimal decimal) {
            NumberFormat df = NumberFormat.getNumberInstance(Locale.ENGLISH);
            df.setMaximumFractionDigits(4);
            df.setMinimumFractionDigits(0);
            df.setGroupingUsed(false);
            return df.format(decimal);
        }

        public static String date(Date date) {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
    }
}