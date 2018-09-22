package com.heidelpay.payment.business.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

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
