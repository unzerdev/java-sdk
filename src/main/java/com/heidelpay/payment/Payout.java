package com.heidelpay.payment;

public class Payout extends AbstractInitPayment {

	private String invoiceId;

	public Payout() {
	}

	public Payout(Heidelpay heidelpay) {
		super(heidelpay);
	}

	@Override
	public String getTypeUrl() {
		return "payments/<paymentId>/payouts";
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

}
