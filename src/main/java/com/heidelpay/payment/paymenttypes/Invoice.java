package com.heidelpay.payment.paymenttypes;

public class Invoice extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/invoice";
	}

}
