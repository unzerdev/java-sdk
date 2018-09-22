package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class ChargeTest extends AbstractPaymentTest {
	
	@Test
	public void testChargeWithTypeId() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}
	
	@Test
	public void testChargeWithPaymentType() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithCustomerTypeReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Customer customer = new Customer("Rene", "Felder");
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"), customer);
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testChargeWithCustomerIdReturnUrl() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), customer.getId());
		assertNotNull(charge);
	}

	@Test
	public void testChargeReturnPayment() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Charge charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getPayment());
		assertNotNull(charge.getPayment().getId());
	}

}
