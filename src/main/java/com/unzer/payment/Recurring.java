package com.unzer.payment;

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

public class Recurring extends AbstractTransaction<Payment> {

    public Recurring() {
        super();
    }

    @Override
    public String getTypeUrl() {
        return "types/<typeId>/recurring";
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }
}
