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
import com.heidelpay.payment.paymenttypes.Sofort;

public class SofortTest extends AbstractPaymentTest {

	@Test
	public void testCreateSofortManatoryType() throws HttpCommunicationException {
		Sofort sofort = new Sofort();
		sofort = getHeidelpay().createPaymentType(sofort);
		assertNotNull(sofort.getId());
	}

	@Test
	public void testChargeSofortType() throws HttpCommunicationException, MalformedURLException {
		Sofort sofort = getHeidelpay().createPaymentType(getSofort());
		Charge charge = sofort.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchSofortType() throws HttpCommunicationException {
		Sofort sofort = getHeidelpay().createPaymentType(getSofort());
		assertNotNull(sofort.getId());
		Sofort fetchedSofort = (Sofort) getHeidelpay().fetchPaymentType(sofort.getId());
		assertNotNull(fetchedSofort.getId());
	}

	
	private Sofort getSofort() {
		Sofort sofort = new Sofort();
		return sofort;
	}


}
