package com.unzer.payment.service;

import com.unzer.payment.Resource;
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

    private static String resolveApiEndpoint(String privateKey) {
        // 'p-' for production, 's-' for sandbox
        return privateKey.charAt(0) == 'p'
                ? "https://api.unzer.com"
                : "https://sbx-api.unzer.com";
    }

    public String getRestUrl() {
        StringBuilder stringBuilder = new StringBuilder(apiEndpoint);
        appendSlashIfNeeded(stringBuilder);
        stringBuilder.append("v1");
        appendSlashIfNeeded(stringBuilder);
        return stringBuilder.toString();
    }

    private void appendSlashIfNeeded(StringBuilder stringBuilder) {
        if (stringBuilder.charAt(stringBuilder.length() - 1) != '/') {
            stringBuilder.append("/");
        }
    }

    public String getUrl(Resource resource) {
        return apiEndpoint + resource.getUrl();
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