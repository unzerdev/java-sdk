package com.heidelpay.payment.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Ignore;
import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class PaymentTest extends AbstractPaymentTest {

	@Test
	public void testFetchPaymentWithAuthorization() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorization());
		assertNotNull(payment.getAuthorization().getId());
	}

	@Test
	public void testFullChargeAfterAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));		
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Charge charge = payment.charge();
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testFetchPaymentWithCharges() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorization());
		assertNotNull(payment.getAuthorization().getId());
		Charge charge = payment.charge();
		payment = charge.getPayment();
		assertNotNull(payment.getChargesList());
		assertEquals(1, payment.getChargesList().size());
		assertEquals(charge.getAmount(), payment.getCharge(0).getAmount());
		assertEquals(charge.getCurrency(), payment.getCharge(0).getCurrency());
		assertEquals(charge.getId(), payment.getCharge(0).getId());
		assertEquals(charge.getReturnUrl(), payment.getCharge(0).getReturnUrl());
		
	}

	@Test
	public void testPartialChargeAfterAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Charge charge = payment.charge(BigDecimal.ONE);
		assertNotNull(charge);
		assertEquals("s-chg-1", charge.getId());
		assertEquals(getBigDecimal("1.0000"), charge.getAmount());
	}

	@Test(expected=HttpCommunicationException.class)
	public void testFullCancelAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Cancel cancel = payment.getAuthorization().cancel();
		assertNotNull(cancel);
		assertEquals("s-cnl-1", cancel.getId());
		assertEquals(authorize.getAmount(), cancel.getAmount());
		payment.cancel();
	}

	@Test
	public void testPartialCancelAuthorize() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentType().getId()));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
		Cancel cancel = payment.cancel(BigDecimal.ONE);
		assertNotNull(cancel);
		assertEquals("s-cnl-1", cancel.getId());
		assertEquals(getBigDecimal("1.0000"), cancel.getAmount());
	}

	@Test
	public void testFullCancelOnCharge() throws HttpCommunicationException, MalformedURLException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Payment payment = getHeidelpay().fetchPayment(charge.getPaymentId());
		Cancel cancel = payment.getCharge("s-chg-1").cancel();
		assertNotNull(cancel);
	}

	@Test
	public void testPartialCancelOnCharge() throws HttpCommunicationException, MalformedURLException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Payment payment = getHeidelpay().fetchPayment(charge.getPaymentId());
		Cancel cancel = payment.getCharge(0).cancel(BigDecimal.ONE);
		assertNotNull(cancel);
	}
	
	@Test
	public void testAuthorize() throws HttpCommunicationException, MalformedURLException{
		// Variant 1
		Payment payment = new Payment(getHeidelpay());
		Authorization authorizationUsingPayment = payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		
		// Variant 2
		Authorization authorizationUsingHeidelpay = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		authorizationUsingHeidelpay.getPayment();
		
		assertNotNull(authorizationUsingPayment);
		assertNotNull(authorizationUsingHeidelpay);
	}
	
	@Test
	public void testChargeWithoutAuthorize() throws HttpCommunicationException, MalformedURLException {
		Charge chargeUsingPayment = new Payment(getHeidelpay()).charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		Charge chargeUsingHeidelpay = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentType().getId(), new URL("https://www.google.at"));
		assertNotNull(chargeUsingPayment);
		assertNotNull(chargeUsingHeidelpay);
	}
}
