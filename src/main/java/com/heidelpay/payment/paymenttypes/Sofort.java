package com.heidelpay.payment.paymenttypes;

public class Sofort extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/sofort";
	}

}
