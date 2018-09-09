package com.heidelpay.payment.paymenttypes;

public class Ideal extends AbstractPaymentType implements PaymentType {

	private String bankName;
	
	@Override
	public String getTypeUrl() {
		return "types/ideal";
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
