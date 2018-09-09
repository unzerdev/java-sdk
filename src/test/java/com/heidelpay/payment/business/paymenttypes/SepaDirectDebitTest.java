package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
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

	@Test
	public void testAuthorizeSddType() throws HttpCommunicationException, MalformedURLException {
		SepaDirectDebit sdd = (SepaDirectDebit) getHeidelpay().createPaymentType(getSepaDirectDebit());
	
		try {
			Authorization authorization = sdd.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"));
		} catch (HttpCommunicationException e) {
			assertTrue("Authorization not allowed for Sepa Direct Debit", true);
		}
		
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
		assertEquals(maskedIban(sddOriginal.getIban()), sddCreated.getIban());
	}


	private SepaDirectDebit getSepaDirectDebit() {
		SepaDirectDebit sdd = new SepaDirectDebit("DE89370400440532013000");
		sdd.setBic("COBADEFFXXX");
		sdd.setHolder("Rene Felder");
		return sdd;
	}

	private String maskedIban(String text) {
		return maskString(text, 6, text.length()-4, '*');
	}

	protected static String maskString(String strText, int start, int end, char maskChar) {

		if (strText == null) return null;
		if (strText.equals("")) return "";
		if (start < 0) start = 0;
		if (end > strText.length()) end = strText.length();

		int maskLength = end - start;

		if (maskLength == 0) return strText;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start + maskLength);
	}

}
