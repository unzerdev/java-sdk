package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Ignore;
import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class AuthorizationTest extends AbstractPaymentTest {

	@Test
	public void testAuthorizeWithAuthorizationObject() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		assertNotNull(authorize.getId());
		assertNotNull(authorize);
	}

	@Test
	public void testAuthorizeWithTypeId() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(authorize);
		assertNotNull(authorize.getId());
	}
	
	@Test
	public void testAuthorizeWithPaymentType() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"));
		assertNotNull(authorize);
		assertNotNull(authorize.getId());
	}

	@Test
	public void testAuthorizeReturnPaymentTypeAndCustomer() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Customer customer = new Customer("Rene", "Felder");
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"), customer);
		Payment payment = authorize.getPayment();
		assertNotNull(payment.getPaymentType());
		assertNotNull(payment.getCustomer());
		assertNotNull(authorize);
	}
	
	@Test
	public void testAuthorizeWithCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), customer.getCustomerId());
		assertNotNull(authorize);
		assertNotNull(authorize.getId());
	}

	@Test
	public void testAuthorizeWithReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(authorize);
		assertNotNull(authorize.getId());
	}

	@Test
	public void testAuthorizeWithCustomerTypeReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Card card = new Card("4444333322221111", "12/19");
		Customer customer = new Customer("Rene", "Felder");
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.google.at"), customer);
		assertNotNull(authorize);
		assertNotNull(authorize.getId());
	}

	@Test
	public void testAuthorizeWithCustomerIdReturnUrl() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"), "rene.felder@felderit.at");
		assertNotNull(authorize);
		assertNotNull(authorize.getId());
	}
	
	@Test
	public void testFetchAuthorization() throws MalformedURLException, HttpCommunicationException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard().getId(), new URL("https://www.google.at"));
		assertNotNull(authorize);
		assertNotNull(authorize.getId());
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization);
		assertNotNull(authorization.getId());
		
	}
	
	
	


}
