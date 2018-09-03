package com.heidelpay.payment.business;

import com.heidelpay.payment.Heidelpay;

public abstract class AbstractPayment {
	private String id;
	private Payment payment;
	private Heidelpay heidelpay;
	
	public AbstractPayment(Heidelpay heidelpay) {
		super();
		this.heidelpay = heidelpay;
	}

	public AbstractPayment() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Heidelpay getHeidelpay() {
		return heidelpay;
	}

}
