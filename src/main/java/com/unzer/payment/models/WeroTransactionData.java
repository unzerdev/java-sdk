package com.unzer.payment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Additional transaction data for Wero payments.
 * Wrapped under the key "wero" inside additionalTransactionData.
 */
@Data
@JsonTypeName("wero")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class WeroTransactionData {

    private EventDependentPayment eventDependentPayment;

    @Data
    public static class EventDependentPayment {
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
    }

    public enum CaptureTrigger {
        @JsonProperty("shipping")
        @SerializedName("shipping")
        SHIPPING,
        @JsonProperty("delivery")
        @SerializedName("delivery")
        DELIVERY,
        @JsonProperty("availability")
        @SerializedName("availability")
        AVAILABILITY,
        @JsonProperty("servicefulfilment")
        @SerializedName("servicefulfilment")
        SERVICEFULFILMENT,
        @JsonProperty("other")
        @SerializedName("other")
        OTHER
    }

    public enum AmountPaymentType {
        @JsonProperty("pay")
        @SerializedName("pay")
        PAY,
        @JsonProperty("payupto")
        @SerializedName("payupto")
        PAYUPTO
    }
}
