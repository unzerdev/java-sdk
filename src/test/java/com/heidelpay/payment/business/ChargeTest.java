package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.business.paymenttypes.Card;

public class ChargeTest extends AbstractPaymentTest {

	@Test
	public void testChargeWithTypeId() throws MalformedURLException {
		Charge Charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1");
		assertNotNull(Charge);
	}
	
	@Test
	public void testChargeWithPaymentType() throws MalformedURLException {
		Card card = new Card("4444333322221111", "12/19");
		Charge Charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card);
		assertNotNull(Charge);
	}

	@Test
	public void testChargeWithCustomerId() throws MalformedURLException {
		Charge Charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1", "rene.felder@felderit.at");
		assertNotNull(Charge);
	}

	@Test
	public void testChargeWithReturnUrl() throws MalformedURLException {
		Charge Charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1", new URL("https://www.google.at"));
		assertNotNull(Charge);
	}

	@Test
	public void testChargeWithCustomerTypeReturnUrl() throws MalformedURLException {
		Card card = new Card("4444333322221111", "12/19");
		Customer customer = new Customer("Rene", "Felder");
		Charge Charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card, customer, new URL("https://www.google.at"));
		assertNotNull(Charge);
	}

	@Test
	public void testChargeWithCustomerIdReturnUrl() throws MalformedURLException {
		Charge Charge = getHeidelpay().charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1", "rene.felder@felderit.at", new URL("https://www.google.at"));
		assertNotNull(Charge);
	}

}
