package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.AbstractPayment;

public class Prepayment extends AbstractPayment implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/prepayment";
	}

}
