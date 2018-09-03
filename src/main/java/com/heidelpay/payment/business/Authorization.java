package com.heidelpay.payment.business;

import java.math.BigDecimal;

import com.heidelpay.payment.Heidelpay;

public class Authorization extends AbstractPayment {

	public Authorization() {
		super();
	}

	public Authorization(Heidelpay heidelpay) {
		super(heidelpay);
	}

	public Charge charge() {
		return getHeidelpay().chargeAuthorization(getPayment().getId());
	}

	public Charge charge(BigDecimal amount) {
		return getHeidelpay().chargeAuthorization(getPayment().getId(), amount);
	}

	public Cancel cancel() {
		return getHeidelpay().cancelAuthorization(getPayment().getId());
	}

	public Cancel cancel(BigDecimal amount) {
		return getHeidelpay().cancelAuthorization(getPayment().getId(), amount);
	}
}
