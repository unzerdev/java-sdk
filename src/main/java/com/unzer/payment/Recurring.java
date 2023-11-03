package com.unzer.payment;

import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.PaymentType;

public class Recurring extends BaseTransaction<Payment> {
  @Override
  protected String getTransactionUrl() {
    return "/v1/types/<typeId>/recurring";
  }
}
