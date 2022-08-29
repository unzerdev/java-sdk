package com.unzer.payment.communication.json;

import com.unzer.payment.models.AdditionalTransactionData;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public class JsonInitPayment extends JsonIdObject implements JsonObject {
    private Boolean isSuccess;
    private Boolean isPending;
    private Boolean isError;
    private JsonMessage message;
    private Date date;

    private String orderId;
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

    public JsonInitPayment() {
        super();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public JsonInitPayment setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public JsonInitPayment setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public URL getReturnUrl() {
        return returnUrl;
    }

    public JsonInitPayment setReturnUrl(URL returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public JsonProcessing getProcessing() {
        return processing;
    }

    public JsonInitPayment setProcessing(JsonProcessing processing) {
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

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Boolean getIsPending() {
        return isPending;
    }

    public void setIsPending(Boolean isPending) {
        this.isPending = isPending;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public Boolean getCard3ds() {
        return card3ds;
    }

    public void setCard3ds(Boolean card3ds) {
        this.card3ds = card3ds;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public BigDecimal getEffectiveInterestRate() {
        return effectiveInterestRate;
    }

    public void setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
        this.effectiveInterestRate = effectiveInterestRate;
    }

    public AdditionalTransactionData getAdditionalTransactionData() {
        return additionalTransactionData;
    }

    public void setAdditionalTransactionData(AdditionalTransactionData additionalTransactionData) {
        this.additionalTransactionData = additionalTransactionData;
    }
}
