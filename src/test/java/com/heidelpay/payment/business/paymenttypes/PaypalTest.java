package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Paypal;

public class PaypalTest extends AbstractPaymentTest {

	@Test
	public void testCreatePaypalManatoryType() throws HttpCommunicationException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getHeidelpay().createPaymentType(paypal);
		assertNotNull(paypal.getId());
	}

	@Test(expected=PaymentException.class)
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		paypal.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));		
	}

	@Test
	public void testChargePaypalType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		Charge charge = paypal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPaypalType() throws HttpCommunicationException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		assertNotNull(paypal.getId());
		Paypal fetchedPaypal = (Paypal) getHeidelpay().fetchPaymentType(paypal.getId());
		assertNotNull(fetchedPaypal.getId());
	}

	
	private Paypal getPaypal() {
		Paypal paypal = new Paypal();
		return paypal;
	}


}
