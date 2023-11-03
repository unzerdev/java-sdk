package com.unzer.payment;

/**
 * First chargeback transaction of a given charge
 */
public class Chargeback extends BaseTransaction<Payment> {
  @Override
  public String getTransactionUrl() {
    return "/v1/payments/<paymentId>/charges/<resourceId>/chargebacks";
  }
}
