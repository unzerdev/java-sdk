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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaypalData that = (PaypalData) o;

        return checkoutType == that.checkoutType;
    }

    @Override
    public int hashCode() {
        return checkoutType != null ? checkoutType.hashCode() : 0;
    }

    public enum CheckoutType {
        @SerializedName("express")
        EXPRESS
    }
}
