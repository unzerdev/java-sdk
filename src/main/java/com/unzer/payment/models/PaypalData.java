package com.unzer.payment.models;

import com.google.gson.annotations.SerializedName;

public class PaypalData {
    private CheckoutType checkoutType;

    public CheckoutType getCheckoutType() {
        return checkoutType;
    }

    public PaypalData setCheckoutType(CheckoutType checkoutType) {
        this.checkoutType = checkoutType;
        return this;
    }

    public enum CheckoutType {
        @SerializedName("express")
        EXPRESS
    }
}
