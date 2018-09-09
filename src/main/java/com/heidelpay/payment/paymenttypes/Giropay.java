package com.heidelpay.payment.paymenttypes;

public class Giropay extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/giropay";
	}

}
