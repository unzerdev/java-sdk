package com.heidelpay.payment.paymenttypes;

public class InvoiceGuaranteed extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/invoice-guaranteed";
	}

}
