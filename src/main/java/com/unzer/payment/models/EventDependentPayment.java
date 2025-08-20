package com.unzer.payment.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Event dependent payment configuration for Wero payments.
 */
@Data
public class EventDependentPayment {
    private CaptureTrigger captureTrigger;
    private AmountPaymentType amountPaymentType;
    /**
     * Maximum time from authorization to capture in seconds.
     */
    private Integer maxAuthToCaptureTime;
    /**
     * Whether multiple captures are allowed.
     */
    private Boolean multiCapturesAllowed;

    public enum CaptureTrigger {
        @SerializedName("shipping")
        SHIPPING,
        @SerializedName("delivery")
        DELIVERY,
        @SerializedName("availability")
        AVAILABILITY,
        @SerializedName("servicefulfilment")
        SERVICEFULFILMENT,
        @SerializedName("other")
        OTHER
    }

    public enum AmountPaymentType {
        @SerializedName("pay")
        PAY,
        @SerializedName("payupto")
        PAYUPTO
    }
}
