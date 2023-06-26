package com.unzer.payment;

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

public class Chargeback extends AbstractTransaction {
  @Override
  public String getTypeUrl() {
    return "payments/{paymentId}/charges/{chargeId}/chargebacks/{chargebackId}";
  }

  @Override
  public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
    return null;
  }
}
