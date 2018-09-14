package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Ideal;

public class IdealTest extends AbstractPaymentTest {

	@Test
	public void testCreateIdealManatoryType() throws HttpCommunicationException {
		Ideal ideal = (Ideal) getHeidelpay().createPaymentType(getIdeal());
		assertNotNull(ideal.getId());
	}

	@Test(expected=HttpCommunicationException.class)
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Ideal ideal = (Ideal) getHeidelpay().createPaymentType(getIdeal());
		ideal.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));		
	}

	@Test
	public void testChargeIdealType() throws HttpCommunicationException, MalformedURLException {
		Ideal ideal = (Ideal) getHeidelpay().createPaymentType(getIdeal());
		Charge charge = ideal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchIdealType() throws HttpCommunicationException {
		Ideal ideal = (Ideal) getHeidelpay().createPaymentType(getIdeal());
		assertNotNull(ideal.getId());
		Ideal fetchedIdeal = (Ideal) getHeidelpay().fetchPaymentType(ideal.getId());
		assertNotNull(fetchedIdeal.getId());
	}

	
	private Ideal getIdeal() {
		Ideal ideal = new Ideal();
		ideal.setBankName("RABONL2U");
		return ideal;
	}


}