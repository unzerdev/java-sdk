package com.unzer.payment.communication.json;

import com.unzer.payment.marketplace.MarketplaceCancelBasket;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public class JsonCancel extends JsonIdObject implements JsonObject {
    private Boolean isSuccess;
    private Boolean isPending;
    private Boolean isError;
    private JsonMessage message;
    private Date date;
    private String paymentReference;

    private BigDecimal amount;
    private Currency currency;
    private URL returnUrl;

    private JsonResources resources;
    private JsonProcessing processing = new JsonProcessing();
    private MarketplaceCancelBasket canceledBasket;

    public JsonCancel() {
        super();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public JsonCancel setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public JsonCancel setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public URL getReturnUrl() {
        return returnUrl;
    }

    public JsonCancel setReturnUrl(URL returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public JsonProcessing getProcessing() {
        return processing;
    }

    public JsonCancel setProcessing(JsonProcessing processing) {
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

    public Boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Boolean isPending() {
        return isPending;
    }

    public void setIsPending(Boolean isPending) {
        this.isPending = isPending;
    }

    public Boolean isError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
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
}
