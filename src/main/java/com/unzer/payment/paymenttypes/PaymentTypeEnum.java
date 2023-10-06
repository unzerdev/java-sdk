package com.unzer.payment.paymenttypes;

public enum PaymentTypeEnum {
  CARD("crd"),
  EPS("eps"),
  GIROPAY("gro"),
  IDEAL("idl"),
  INVOICE("ivc"),
  @Deprecated
  INVOICE_GUARANTEED("ivg"),
  @Deprecated
  INVOICE_FACTORING("ivf"),
  @Deprecated
  INVOICE_SECURED("ivs"),
  PAYPAL("ppl"),
  PREPAYMENT("ppy"),
  PRZELEWY24("p24"),
  PAYU("pyu"),
  SEPA_DIRECT_DEBIT("sdd"),
  SEPA_DIRECT_DEBIT_GUARANTEED("ddg"),
  SEPA_DIRECT_DEBIT_SECURED("dds"),
  SOFORT("sft"),
  PIS("pis"),
  ALIPAY("ali"),
  WECHATPAY("wcp"),
  APPLEPAY("apl"),
  HIRE_PURCHASE_RATE_PLAN("hdd"),
  @Deprecated
  INSTALLMENT_SECURED_RATE_PLAN("ins"),
  BANCONTACT("bct"),
  PF_CARD("pfc"),
  PF_EFINANCE("pfe"),
  UNZER_PAYLATER_INVOICE("piv"),
  KLARNA("kla"),
  PAYLATER_INSTALLMENT("pit"),
  PAYLATER_DIRECT_DEBIT("pdd"),
  UNKNOWN("unknown");

  private final String shortName;

  PaymentTypeEnum(String shortName) {
    this.shortName = shortName;
  }

  public static PaymentTypeEnum getPaymentTypeEnumByShortName(String shortName) {
    for (PaymentTypeEnum typeEnum : PaymentTypeEnum.values()) {
      if (typeEnum.getShortName().equals(shortName)) {
        return typeEnum;
      }
    }

    return PaymentTypeEnum.UNKNOWN;
  }

  public String getShortName() {
    return this.shortName;
  }
}
