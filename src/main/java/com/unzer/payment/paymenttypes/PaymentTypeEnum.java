package com.unzer.payment.paymenttypes;

import java.util.Arrays;

public enum PaymentTypeEnum {
    CARD("crd"),
    CLICK_TO_PAY("ctp"),
    EPS("eps"),
    @Deprecated
    GIROPAY("gro"),
    GOOGLE_PAY("gop"),
    IDEAL("idl"),
    @Deprecated
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
    @Deprecated
    SEPA_DIRECT_DEBIT_GUARANTEED("ddg"),
    @Deprecated
    SEPA_DIRECT_DEBIT_SECURED("dds"),
    @Deprecated
    SOFORT("sft"),
    @Deprecated
    PIS("pis"),
    ALIPAY("ali"),
    WECHATPAY("wcp"),
    APPLEPAY("apl"),
    @Deprecated
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
    TWINT("twt"),
    OPEN_BANKING("obp"),
    WERO("wro"),
    UNKNOWN("unknown");

    private final String shortName;

    PaymentTypeEnum(String shortName) {
        this.shortName = shortName;
    }

    public static PaymentTypeEnum getPaymentTypeEnumByShortName(String shortName) {
        return Arrays.stream(values())
                .filter(t -> t.shortName.equals(shortName))
                .findFirst()
                .orElse(PaymentTypeEnum.UNKNOWN);
    }
}
