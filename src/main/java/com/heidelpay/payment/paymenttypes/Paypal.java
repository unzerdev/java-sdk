package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.AbstractPayment;

public class Paypal extends AbstractPayment implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/paypal";
	}

}
