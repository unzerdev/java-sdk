package com.unzer.payment.communication.json;

import com.unzer.payment.marketplace.MarketplaceCancelBasket;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public class ApiCancel extends ApiIdObject implements ApiObject, TransactionStatus {
    private Boolean isSuccess;
    private Boolean isPending;
    private Boolean isError;

    private JsonMessage message;
    private Date date;
    private String paymentReference;
    private String reasonCode;

    private String orderId;
    private String invoiceId;

    private BigDecimal amount;
    private Currency currency;
    private URL returnUrl;

    private JsonResources resources;
    private JsonProcessing processing = new JsonProcessing();
    private MarketplaceCancelBasket canceledBasket;

    public BigDecimal getAmount() {
        return amount;
    }

    public ApiCancel setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public ApiCancel setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public URL getReturnUrl() {
        return returnUrl;
    }

    public ApiCancel setReturnUrl(URL returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public JsonProcessing getProcessing() {
        return processing;
    }

    public ApiCancel setProcessing(JsonProcessing processing) {
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

    @Override
    public Boolean getResumed() {
        return null;
    }

    @Override
    public void setResumed(Boolean value) {
        // Do nothing
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean isError) {
        this.isError = isError;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public MarketplaceCancelBasket getCanceledBasket() {
        return canceledBasket;
    }

    public void setCanceledBasket(MarketplaceCancelBasket canceledBasket) {
        this.canceledBasket = canceledBasket;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
