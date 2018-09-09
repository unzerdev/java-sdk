package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.AbstractPayment;

public class Eps extends AbstractPayment implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/eps";
	}

}
