package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.SepaDirectDebitGuaranteed;

public class SepaDirectDebitGuaranteedTest extends AbstractPaymentTest {

	@Test
	public void testCreateSepaDirectDebitGuaranteedManatoryType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sdd = new SepaDirectDebitGuaranteed("DE89370400440532013000");
		sdd = (SepaDirectDebitGuaranteed) getHeidelpay().createPaymentType(sdd);
		assertNotNull(sdd.getId());
	}

	@Test
	public void testCreateSepaDirectDebitGuaranteedFullType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sddOriginal = getSepaDirectDebitGuaranteed();
		SepaDirectDebitGuaranteed sddCreated = (SepaDirectDebitGuaranteed) getHeidelpay().createPaymentType(sddOriginal);
		assertSddEquals(sddOriginal, sddCreated);
	}

	@Test
	public void testAuthorizeSddType() throws HttpCommunicationException, MalformedURLException {
		SepaDirectDebitGuaranteed sdd = (SepaDirectDebitGuaranteed) getHeidelpay().createPaymentType(getSepaDirectDebitGuaranteed());
	
		try {
			sdd.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));
		} catch (HttpCommunicationException e) {
			assertTrue("Authorization not allowed for Sepa Direct Debit", true);
		}
		
	}

	@Test
	// TODO Should we put the charge method into specific classes?
	public void testChargeSepaDirectDebitGuaranteedType() throws HttpCommunicationException, MalformedURLException, ParseException {
		SepaDirectDebitGuaranteed sdd = (SepaDirectDebitGuaranteed) getHeidelpay().createPaymentType(getSepaDirectDebitGuaranteed());
		Charge charge = sdd.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"), getMaximumCustomer(getRandomId()));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testFetchSepaDirectDebitGuaranteedType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sdd = (SepaDirectDebitGuaranteed) getHeidelpay().createPaymentType(getSepaDirectDebitGuaranteed());
		assertNotNull(sdd.getId());
		SepaDirectDebitGuaranteed fetchedSdd = (SepaDirectDebitGuaranteed) getHeidelpay().fetchPaymentType(sdd.getId());
		assertNotNull(fetchedSdd.getId());
		assertSddEquals(sdd, fetchedSdd);
	}

	
	private void assertSddEquals(SepaDirectDebitGuaranteed sddOriginal, SepaDirectDebitGuaranteed sddCreated) {
		assertNotNull(sddCreated.getId());
		assertEquals(sddOriginal.getBic(), sddCreated.getBic());
		assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
		assertEquals(maskIban(sddOriginal.getIban()), sddCreated.getIban());
	}


	private SepaDirectDebitGuaranteed getSepaDirectDebitGuaranteed() {
		SepaDirectDebitGuaranteed sdd = new SepaDirectDebitGuaranteed("DE89370400440532013000");
		sdd.setBic("COBADEFFXXX");
		sdd.setHolder("Rene Felder");
		return sdd;
	}


}
