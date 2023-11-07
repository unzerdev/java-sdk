package com.unzer.payment.communication.json;

import java.util.Currency;
import java.util.List;

public class ApiPayment extends ApiIdObject {

    private JsonState state;
    private JsonAmount amount;
    private Currency currency;
    private String orderId;
    private JsonResources resources;
    private List<ApiTransaction> transactions;

    public JsonState getState() {
        return state;
    }

    public void setState(JsonState state) {
        this.state = state;
    }

    public JsonAmount getAmount() {
        return amount;
    }

    public void setAmount(JsonAmount amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public JsonResources getResources() {
        return resources;
    }

    public void setResources(JsonResources resources) {
        this.resources = resources;
    }

    public List<ApiTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<ApiTransaction> transactions) {
        this.transactions = transactions;
    }


}
