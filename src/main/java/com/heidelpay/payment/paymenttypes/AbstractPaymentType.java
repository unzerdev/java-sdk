package com.heidelpay.payment.paymenttypes;

import com.heidelpay.payment.Heidelpay;

public abstract class AbstractPaymentType implements PaymentType {
	private String id;
	
	private transient Heidelpay heidelpay;
	
	public AbstractPaymentType(Heidelpay heidelpay) {
		super();
		this.setHeidelpay(heidelpay);
	}

	public AbstractPaymentType() {
		super();
	}

	public abstract String getTypeUrl();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Heidelpay getHeidelpay() {
		return heidelpay;
	}

	public void setHeidelpay(Heidelpay heidelpay) {
		this.heidelpay = heidelpay;
	}

	// Currently returnUrl is mandatory	
//		public Charge charge(BigDecimal amount, Currency currency) throws HttpCommunicationException {
//			return getHeidelpay().charge(amount, currency, this);
//		}
//		public Authorization authorize(BigDecimal amount, Currency currency) throws HttpCommunicationException {
//			return authorize(amount, currency, (URL)null, (Customer)null);
//		}
		


}
