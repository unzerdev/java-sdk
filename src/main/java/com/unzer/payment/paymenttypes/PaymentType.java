package com.unzer.payment.paymenttypes;

import com.unzer.payment.communication.json.JsonObject;

public interface PaymentType {
    String getTypeUrl();

    String getId();

    PaymentType map(PaymentType paymentType, JsonObject jsonObject);
}
