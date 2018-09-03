package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.business.paymenttypes.Card;
import com.heidelpay.payment.business.paymenttypes.PaymentType;

public class AuthorizationTest extends AbstractPaymentTest {

	@Test
	public void testAuthorizeWithTypeId() throws MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1");
		assertNotNull(authorize);
	}
	
	@Test
	public void testAuthorizeWithPaymentType() throws MalformedURLException {
		Card card = new Card("4444333322221111", "12/19");
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card);
		Payment payment = authorize.getPayment();
		PaymentType paymentType = payment.getPaymentType();
		Customer customer = payment.getCustomer();
		assertNotNull(authorize);
	}

	@Test
	public void testAuthorizeWithCustomerId() throws MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1", "rene.felder@felderit.at");
		assertNotNull(authorize);
	}

	@Test
	public void testAuthorizeWithReturnUrl() throws MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1", new URL("https://www.google.at"));
		assertNotNull(authorize);
	}

	@Test
	public void testAuthorizeWithCustomerTypeReturnUrl() throws MalformedURLException {
		Card card = new Card("4444333322221111", "12/19");
		Customer customer = new Customer("Rene", "Felder");
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, customer, new URL("https://www.google.at"));
		assertNotNull(authorize);
	}

	@Test
	public void testAuthorizeWithCustomerIdReturnUrl() throws MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-1", "rene.felder@felderit.at", new URL("https://www.google.at"));
		assertNotNull(authorize);
	}
}
