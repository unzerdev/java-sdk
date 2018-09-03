package com.heidelpay.payment.business;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PaymentTest extends AbstractPaymentTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFullChargeAfterAuthorize() {
		Payment payment = getHeidelpay().fetchPayment("s-pay-1");
		payment.charge();
	}

	@Test
	public void testPartialChargeAfterAuthorize() {
		Payment payment = getHeidelpay().fetchPayment("s-pay-1");
		Charge charge = payment.charge(BigDecimal.ONE);
		assertNotNull(charge);
	}

	@Test
	public void testFullCancelAuthorize() {
		Payment payment = getHeidelpay().fetchPayment("s-pay-1");
		Cancel cancel = payment.getAuthorization().cancel();
		Cancel cancel2 = payment.cancel();
		assertNotNull(cancel);
	}

	@Test
	public void testPartialCancelAuthorize() {
		Payment payment = getHeidelpay().fetchPayment("s-pay-1");
		Cancel cancel = payment.cancel(BigDecimal.ONE);
		assertNotNull(cancel);
	}

	@Test
	public void testFullCancelOnCharge() {
		Payment payment = getHeidelpay().fetchPayment("s-pay-1");
		Cancel cancel = payment.getCharge("s-chg-1").cancel();
		assertNotNull(cancel);
	}

	@Test
	public void testPartialCancelOnCharge() {
		Payment payment = getHeidelpay().fetchPayment("s-pay-1");
		Cancel cancel = payment.getCharge(1).cancel(BigDecimal.ONE);
		assertNotNull(cancel);
	}
	
	@Test
	public void testAuthorize() {
		// Variant 1
		Payment payment = new Payment(getHeidelpay());
		Authorization authorizationUsingPayment = payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1");
		
		// Variant 2
		Authorization authorizationUsingHeidelpay = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1");
		Payment pay = authorizationUsingHeidelpay.getPayment();
		
		assertNotNull(authorizationUsingPayment);
		assertNotNull(authorizationUsingHeidelpay);
	}
	
	@Test
	public void testChargeWithoutAuthorize() {
		Charge chargeUsingPayment = new Payment(getHeidelpay()).charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1");
		Charge chargeUsingHeidelpay = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1");
		assertNotNull(chargeUsingPayment);
		assertNotNull(chargeUsingHeidelpay);
	}
}
