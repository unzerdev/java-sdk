package com.heidelpay.payment.paymenttypes;

public class Eps extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/eps";
	}

}
