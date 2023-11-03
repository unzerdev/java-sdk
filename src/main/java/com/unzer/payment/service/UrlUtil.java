package com.unzer.payment.service;

import com.unzer.payment.BasePaypage;
import com.unzer.payment.Basket;
import com.unzer.payment.Recurring;
import com.unzer.payment.models.PaylaterInvoiceConfigRequest;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaymentType;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class UrlUtil {
  private static final String PRODUCTION_ENDPOINT = "https://api.unzer.com";
  private static final String SANDBOX_ENDPOINT = "https://sbx-api.unzer.com";
  private static final char PRODUCTION_KEY_PREFIX = 'p';
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
    stringBuilder.append(APIVERSION_1);
    appendSlashIfNeeded(stringBuilder);
    return stringBuilder.toString();
  }

  private void appendSlashIfNeeded(StringBuilder stringBuilder) {
    if (stringBuilder.charAt(stringBuilder.length() - 1) != '/') {
      stringBuilder.append("/");
    }
  }

  public String getPaymentUrl(PaymentType paymentType, String paymentId, String id) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getRestUrlInternal(paymentType));
    appendSlashIfNeeded(stringBuilder);
    stringBuilder.append(id);
    String result = stringBuilder.toString();
    return result.replace(PLACEHOLDER_PAYMENT_ID, paymentId);
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

  public String getPaymentUrl(PaymentType paymentType, String paymentId) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getRestUrlInternal(paymentType));
    appendSlashIfNeeded(stringBuilder);
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

  public String getRestUrl(PaymentType paymentType) {
    return getRestUrlInternal(paymentType).replace(PLACEHOLDER_PAYMENT_ID + "/", "");
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

  public String getRecurringUrl(Recurring recurring) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getRestUrl());
    appendSlashIfNeeded(stringBuilder);
    stringBuilder.append(RECURRING_URL);
    String result = stringBuilder.toString();
    result = result.replace(PLACEHOLDER_TYPE_ID, recurring.getTypeId());
    return result;
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

  public String getInitPaypageUrl(BasePaypage page) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getRestUrl());
    appendSlashIfNeeded(stringBuilder);
    stringBuilder.append(page.getTypeUrl());
    appendSlashIfNeeded(stringBuilder);

    String action = Optional
        .ofNullable(page.getAction())
        .orElse(BasePaypage.Action.CHARGE)
        .toLowerCase();
    stringBuilder.append(action);

    return stringBuilder.toString();
  }

  public String getPaymentTypeConfigUrl(PaylaterInvoiceConfigRequest configRequest) {
    return apiEndpoint + configRequest.getRequestUrl();
  }

  public String getInstallmentPlanUrl(InstallmentPlansRequest installmentPlansRequest) {
    return apiEndpoint + installmentPlansRequest.getRequestUrl();
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
