package com.heidelpay.payment.business;

import com.heidelpay.payment.Heidelpay;

public class AbstractPaymentTest {
	private Heidelpay heidelpay = new Heidelpay("s-priv-6S59Dt6Q9mJYj8X5qpcxSpA3XLXUw4Zf");

	public Heidelpay getHeidelpay() {
		return heidelpay;
	}

}
