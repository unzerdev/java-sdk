package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Ignore;
import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.SepaDirectDebitGuaranteed;

public class SepaDirectDebitGuaranteedTest extends AbstractPaymentTest {

	@Test
	public void testCreateSepaDirectDebitGuaranteedManatoryType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sdd = new SepaDirectDebitGuaranteed("DE89370400440532013000");
		sdd = getHeidelpay().createPaymentType(sdd);
		assertNotNull(sdd.getId());
	}

	@Test
	public void testCreateSepaDirectDebitGuaranteedFullType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sddOriginal = getSepaDirectDebitGuaranteed();
		SepaDirectDebitGuaranteed sddCreated = getHeidelpay().createPaymentType(sddOriginal);
		assertSddEquals(sddOriginal, sddCreated);
	}

	@Test
	@Ignore("Created Bug AHC-302 for Shipment")
	public void testShipmentSepaDirectDebitGuaranteedType() throws HttpCommunicationException, MalformedURLException, ParseException {
		Charge charge = getHeidelpay().charge(BigDecimal.TEN, Currency.getInstance("EUR"), new SepaDirectDebitGuaranteed("DE89370400440532013000"), new URL("https://www.google.at"), getMaximumCustomer(getRandomId()));
		Shipment shipment = getHeidelpay().shipment(charge.getPaymentId());
		assertNotNull(shipment);
		assertNotNull(shipment.getId());
	}

	@Test
	public void testFetchSepaDirectDebitGuaranteedType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sdd = getHeidelpay().createPaymentType(getSepaDirectDebitGuaranteed());
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
