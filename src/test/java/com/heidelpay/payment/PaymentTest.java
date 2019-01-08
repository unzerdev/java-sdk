package com.heidelpay.payment;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaymentTest {

	@Test
	public void testIsNotEmpty() {
		Payment payment = new Payment(getHeidelpay());
		assertFalse(payment.isNotEmpty(""));
		assertFalse(payment.isNotEmpty("   "));
		assertFalse(payment.isNotEmpty(null));
		assertTrue(payment.isNotEmpty("a"));
	}

	private Heidelpay getHeidelpay() {
		return null;
	}

}
