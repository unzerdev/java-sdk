package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.AbstractPayment;

public class Ideal extends AbstractPayment implements PaymentType {

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
