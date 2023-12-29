package com.unzer.payment.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalData {
    private CheckoutType checkoutType;

    public enum CheckoutType {
        @SerializedName("express")
        EXPRESS
    }
}
