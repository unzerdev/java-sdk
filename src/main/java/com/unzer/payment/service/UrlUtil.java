package com.unzer.payment.service;

import com.unzer.payment.Basket;
import com.unzer.payment.Recurring;
import com.unzer.payment.Resource;
import com.unzer.payment.models.PaylaterInvoiceConfigRequest;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaymentType;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class UrlUtil {
  private static final String PRODUCTION_ENDPOINT = "https://api.unzer.com";
  private static final String SANDBOX_ENDPOINT = "https://sbx-api.unzer.com";
  private static final char PRODUCTION_KEY_PREFIX = 'p';
  private static final String PLACEHOLDER_CHARGE_ID = "<chargeId>";
  private static final String PLACEHOLDER_PAYMENT_ID = "<paymentId>";
  private static final String REFUND_URL = "payments/<paymentId>/charges/<chargeId>/cancels";

  private final String apiEndpoint;

  public UrlUtil(String privateKey) {
    this.apiEndpoint = resolveApiEndpoint(privateKey);
  }

  private static String resolveApiEndpoint(String privateKey) {
    // 'p-' for production, 's-' for sandbox
    return privateKey.charAt(0) == PRODUCTION_KEY_PREFIX
        ? PRODUCTION_ENDPOINT
        : SANDBOX_ENDPOINT;
  }

  public String getRefundUrl(String paymentId, String chargeId) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getRestUrl());
    appendSlashIfNeeded(stringBuilder);
    stringBuilder.append(REFUND_URL);
    String result = stringBuilder.toString();
    result = result.replace(PLACEHOLDER_PAYMENT_ID, paymentId);
    if (chargeId != null) {
      result = result.replace(PLACEHOLDER_CHARGE_ID, chargeId);
    } else {
      result = result.replace(PLACEHOLDER_CHARGE_ID + "/", "");
    }
    return result;
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

  public String getHttpGetUrl(String id) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getRestUrlWithOutPaymentType());
    appendSlashIfNeeded(stringBuilder);
    stringBuilder.append(id);
    return stringBuilder.toString();
  }

  public String getRestUrlWithOutPaymentType() {
    return getRestUrlInternal().replace(PLACEHOLDER_PAYMENT_ID + "/", "");
  }

  private String getRestUrlInternal() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getRestUrl());
    appendSlashIfNeeded(stringBuilder);
    stringBuilder.append("types");
    appendSlashIfNeeded(stringBuilder);
    return stringBuilder.toString();
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
