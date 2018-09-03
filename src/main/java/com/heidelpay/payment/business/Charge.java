package com.heidelpay.payment.business;

import java.math.BigDecimal;

import com.heidelpay.payment.Heidelpay;

public class Charge extends AbstractPayment {
	public Charge() {
		super();
	}
	public Charge(Heidelpay heidelpay) {
		super(heidelpay);
	}
	
	public Cancel cancel() {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId());
	}
	public Cancel cancel(BigDecimal amount) {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId(), amount);
	}

}
