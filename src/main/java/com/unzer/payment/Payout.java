package com.unzer.payment;

import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.PaymentType;

public class Payout extends AbstractTransaction<Payment> {

  public Payout() {
  }

  @Deprecated
  public Payout(Unzer unzer) {
    super(unzer);
  }

  @Override
  public String getTypeUrl() {
    return "payments/<paymentId>/payouts";
  }

  @Override
  public PaymentType map(PaymentType paymentType, ApiObject apiObject) {
    return null;
  }

}
