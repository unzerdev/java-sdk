package com.unzer.payment;

import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.PaymentType;

public class Chargeback extends AbstractTransaction {
  @Override
  public String getTypeUrl() {
    return "payments/{paymentId}/charges/{chargeId}/chargebacks/{chargebackId}";
  }

  @Override
  public PaymentType map(PaymentType paymentType, ApiObject apiObject) {
    return null;
  }
}
