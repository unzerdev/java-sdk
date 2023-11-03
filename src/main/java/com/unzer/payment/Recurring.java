package com.unzer.payment;

public class Recurring extends BaseTransaction<Payment> {
  @Override
  protected String getTransactionUrl() {
    return "/v1/types/<typeId>/recurring";
  }
}
