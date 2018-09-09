package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.AbstractPayment;

public class InvoiceGuaranteed extends AbstractPayment implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/invoice-guaranteed";
	}

}
