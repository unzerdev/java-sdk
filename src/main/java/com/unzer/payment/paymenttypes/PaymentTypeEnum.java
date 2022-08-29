package com.unzer.payment.paymenttypes;

public enum PaymentTypeEnum {
    CARD("crd"),
    EPS("eps"),
    GIROPAY("gro"),
    IDEAL("idl"),
    INVOICE("ivc"),
    INVOICE_GUARANTEED("ivg"),
    INVOICE_FACTORING("ivf"),
    INVOICE_SECURED("ivs"),
    PAYPAL("ppl"),
    PREPAYMENT("ppy"),
    PRZELEWY24("p24"),
    SEPA_DIRECT_DEBIT("sdd"),
    SEPA_DIRECT_DEBIT_GUARANTEED("ddg"),
    SEPA_DIRECT_DEBIT_SECURED("dds"),
    SOFORT("sft"),
    PIS("pis"),
    ALIPAY("ali"),
    WECHATPAY("wcp"),
    APPLEPAY("apl"),
    HIRE_PURCHASE_RATE_PLAN("hdd"),
    INSTALLMENT_SECURED_RATE_PLAN("ins"),
    BANCONTACT("bct"),
    PF_CARD("pfc"),
    PF_EFINANCE("pfe"),
    UNKNOWN("unknown");

    private String shortName;

    private PaymentTypeEnum(String shortName) {
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
