package com.heidelpay.payment;

public class Shipment extends AbstractPayment {

	@Override
	public String getTypeUrl() {
		return "payments/<paymentId>/shipments";
	}

}
