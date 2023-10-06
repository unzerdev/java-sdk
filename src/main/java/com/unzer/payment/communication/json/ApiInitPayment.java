package com.unzer.payment.communication.json;

import com.unzer.payment.models.AdditionalTransactionData;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public class ApiInitPayment extends ApiIdObject implements ApiObject, TransactionStatus {
  private Boolean isSuccess;
  private Boolean isPending;
  private Boolean isError;
  private Boolean isResumed;
  private JsonMessage message;
  private Date date;

  private String orderId;
  private String invoiceId;
  private BigDecimal amount;
  private Currency currency;
  private URL returnUrl;
  private URL redirectUrl;
  private Boolean card3ds;
  private String paymentReference;
  private BigDecimal effectiveInterestRate;
  private AdditionalTransactionData additionalTransactionData;

  private JsonResources resources;
  private JsonProcessing processing = new JsonProcessing();

  public ApiInitPayment() {
    super();
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public ApiInitPayment setAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public Currency getCurrency() {
    return currency;
  }

  public ApiInitPayment setCurrency(Currency currency) {
    this.currency = currency;
    return this;
  }

  public URL getReturnUrl() {
    return returnUrl;
  }

  public ApiInitPayment setReturnUrl(URL returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }

  public JsonProcessing getProcessing() {
    return processing;
  }

  public ApiInitPayment setProcessing(JsonProcessing processing) {
    this.processing = processing;
    return this;
  }


  public JsonResources getResources() {
    return resources;
  }

  public void setResources(JsonResources resources) {
    this.resources = resources;
  }

  public JsonMessage getMessage() {
    return message;
  }

  public void setMessage(JsonMessage message) {
    this.message = message;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public URL getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(URL redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public Boolean getSuccess() {
    return isSuccess;
  }

  public void setSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public Boolean getPending() {
    return isPending;
  }

  public void setPending(Boolean isPending) {
    this.isPending = isPending;
  }

  public Boolean getError() {
    return isError;
  }

  public void setError(Boolean isError) {
    this.isError = isError;
  }

  public Boolean getResumed() {
    return isResumed;
  }

  public void setResumed(Boolean resumed) {
    isResumed = resumed;
  }

  public Boolean getCard3ds() {
    return card3ds;
  }

  public ApiInitPayment setCard3ds(Boolean card3ds) {
    this.card3ds = card3ds;
    return this;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public ApiInitPayment setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
    return this;
  }

  public BigDecimal getEffectiveInterestRate() {
    return effectiveInterestRate;
  }

  public ApiInitPayment setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
    this.effectiveInterestRate = effectiveInterestRate;
    return this;
  }

  public AdditionalTransactionData getAdditionalTransactionData() {
    return additionalTransactionData;
  }

  public ApiInitPayment setAdditionalTransactionData(
      AdditionalTransactionData additionalTransactionData) {
    this.additionalTransactionData = additionalTransactionData;
    return this;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public ApiInitPayment setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
    return this;
  }
}
