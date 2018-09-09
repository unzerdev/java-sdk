package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.AbstractPayment;

public class Przelewy24 extends AbstractPayment implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/przelewy24";
	}

}
