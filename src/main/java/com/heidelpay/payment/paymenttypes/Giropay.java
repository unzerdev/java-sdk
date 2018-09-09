package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.AbstractPayment;

public class Giropay extends AbstractPayment implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/giropay";
	}

}
