package com.unzer.payment;

public class Recurring extends BaseTransaction<Payment> {
  private final static String TYPE_ID_TOKEN = "<typeId>";

  @Override
  public String getUrl() {
    String partialResult = getPaymentId() == null
        ? getTransactionUrl().replaceAll(PAYMENT_ID_TOKEN + "/", "")
        : getTransactionUrl().replaceAll(PAYMENT_ID_TOKEN, getPaymentId());

    partialResult = getTypeId() == null
        ? partialResult.replaceAll(TYPE_ID_TOKEN + "/", "")
        : partialResult.replaceAll(TYPE_ID_TOKEN, getTypeId());

    return partialResult
        .replaceAll(TRANSACTION_ID_TOKEN, getId() == null ? "" : getId());
  }
  @Override
  protected String getTransactionUrl() {
    return "/v1/types/<typeId>/recurring";
  }
}
