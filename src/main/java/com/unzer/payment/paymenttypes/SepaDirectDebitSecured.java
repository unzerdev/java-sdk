package com.unzer.payment.paymenttypes;

@Deprecated
public class SepaDirectDebitSecured extends SepaDirectDebit implements PaymentType {

  public SepaDirectDebitSecured(String iban) {
    super(iban);
  }

  @Override
  public String getResourceUrl() {
    return "/v1/types/sepa-direct-debit-secured/<resourceId>";
  }

}
