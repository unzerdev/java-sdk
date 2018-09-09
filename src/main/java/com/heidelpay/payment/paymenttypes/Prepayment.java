package com.heidelpay.payment.paymenttypes;

public class Prepayment extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/prepayment";
	}

}
