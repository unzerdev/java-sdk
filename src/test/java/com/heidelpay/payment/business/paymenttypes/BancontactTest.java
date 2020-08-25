package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Bancontact;

public class BancontactTest extends AbstractPaymentTest {

	@Test
	public void testCreateBancontactWithoutHolder() throws HttpCommunicationException {
		Bancontact bacncontact = new Bancontact();
		bacncontact = getHeidelpay().createPaymentType(bacncontact);
		assertNotNull(bacncontact.getId());
	}

	@Test
	public void testCreateBancontactWithHolder() throws HttpCommunicationException {
		Bancontact bancontact = new Bancontact("test holder");
		bancontact = getHeidelpay().createPaymentType(bancontact);
		assertNotNull(bancontact.getId());
		assertEquals("test holder", bancontact.getHolder());
	}

	@Test
	public void testChargeBancontactType() throws HttpCommunicationException, MalformedURLException {
		Bancontact bancontact = getHeidelpay().createPaymentType(getBancontact());
		Charge charge = bancontact.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchBancontactType() throws HttpCommunicationException {
		Bancontact bancontact = getHeidelpay().createPaymentType(getBancontact());
		assertNotNull(bancontact.getId());
		Bancontact fetchedBancontact = (Bancontact) getHeidelpay().fetchPaymentType(bancontact.getId());
		assertNotNull(fetchedBancontact.getId());
	}
	
	private Bancontact getBancontact() {
		Bancontact bancontact = new Bancontact();
		return bancontact;
	}
}
