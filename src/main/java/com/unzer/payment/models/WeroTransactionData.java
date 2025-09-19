package com.unzer.payment.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

/**
 * Additional transaction data for Wero payments.
 * Wrapped under the key "wero" inside additionalTransactionData.
 */
@Data
@JsonTypeName("wero")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class WeroTransactionData {
    private WeroEventDependentPayment eventDependentPayment;
}
