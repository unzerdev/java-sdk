package com.heidelpay.payment.paymenttypes;

public class Paypal extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/paypal";
	}

}
