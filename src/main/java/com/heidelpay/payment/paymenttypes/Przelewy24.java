package com.heidelpay.payment.paymenttypes;

public class Przelewy24 extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/przelewy24";
	}

}
