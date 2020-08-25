package com.heidelpay.payment.paymenttypes;

public enum PaymentTypeEnum {
	CARD("crd"), EPS("eps"), GIROPAY("gro"), IDEAL("idl"), INVOICE("ivc"),
	INVOICE_GUARANTEED("ivg"), INVOICE_FACTORING("ivf"), PAYPAL("ppl"), PREPAYMENT("ppy"), PRZELEWY24("p24"),
	SEPA_DIRECT_DEBIT("sdd"), SEPA_DIRECT_DEBIT_GUARANTEED("ddg"), SOFORT("sft"), PIS("pis"), ALIPAY("ali"),
	WECHATPAY("wcp"), APPLEPAY("apl"), HIRE_PURCHASE_RATE_PLAN("hdd"), BANCONTACT("bct"),
	UNKNOWN("unknown");
	
	private String shortName;
	
	private PaymentTypeEnum(String shortName) {
		this.shortName = shortName;
	}
	
	public String getShortName() {
		return this.shortName;
	}
	
	public static PaymentTypeEnum getPaymentTypeEnumByShortName(String shortName) {
		for(PaymentTypeEnum typeEnum : PaymentTypeEnum.values()) {
			if(typeEnum.getShortName().equals(shortName)) {
				return typeEnum;
			}
		}
		
		return PaymentTypeEnum.UNKNOWN;
	}
}
