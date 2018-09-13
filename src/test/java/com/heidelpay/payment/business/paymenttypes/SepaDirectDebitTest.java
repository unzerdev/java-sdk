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
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;

public class SepaDirectDebitTest extends AbstractPaymentTest {

	@Test
	public void testCreateSepaDirectDebitManatoryType() throws HttpCommunicationException {
		SepaDirectDebit sdd = new SepaDirectDebit("DE89370400440532013000");
		sdd = (SepaDirectDebit) getHeidelpay().createPaymentType(sdd);
		assertNotNull(sdd.getId());
	}

	@Test
	public void testCreateSepaDirectDebitFullType() throws HttpCommunicationException {
		SepaDirectDebit sddOriginal = getSepaDirectDebit();
		SepaDirectDebit sddCreated = (SepaDirectDebit) getHeidelpay().createPaymentType(sddOriginal);
		assertSddEquals(sddOriginal, sddCreated);
	}

	@Test(expected=HttpCommunicationException.class)
	public void testAuthorizeSddType() throws HttpCommunicationException, MalformedURLException {
		SepaDirectDebit sdd = (SepaDirectDebit) getHeidelpay().createPaymentType(getSepaDirectDebit());
		sdd.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));		
	}

	@Test
	public void testChargeSepaDirectDebitType() throws HttpCommunicationException, MalformedURLException {
		SepaDirectDebit sdd = (SepaDirectDebit) getHeidelpay().createPaymentType(getSepaDirectDebit());
		Charge charge = sdd.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testFetchSepaDirectDebitType() throws HttpCommunicationException {
		SepaDirectDebit sdd = (SepaDirectDebit) getHeidelpay().createPaymentType(getSepaDirectDebit());
		assertNotNull(sdd.getId());
		SepaDirectDebit fetchedSdd = (SepaDirectDebit) getHeidelpay().fetchPaymentType(sdd.getId());
		assertNotNull(fetchedSdd.getId());
		assertSddEquals(sdd, fetchedSdd);
	}

	
	private void assertSddEquals(SepaDirectDebit sddOriginal, SepaDirectDebit sddCreated) {
		assertNotNull(sddCreated.getId());
		assertEquals(sddOriginal.getBic(), sddCreated.getBic());
		assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
		assertEquals(maskIban(sddOriginal.getIban()), sddCreated.getIban());
	}


	private SepaDirectDebit getSepaDirectDebit() {
		SepaDirectDebit sdd = new SepaDirectDebit("DE89370400440532013000");
		sdd.setBic("COBADEFFXXX");
		sdd.setHolder("Rene Felder");
		return sdd;
	}


}
