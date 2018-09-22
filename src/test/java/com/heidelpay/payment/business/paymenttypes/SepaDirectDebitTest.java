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
		sdd = getHeidelpay().createPaymentType(sdd);
		assertNotNull(sdd.getId());
	}

	@Test
	public void testCreateSepaDirectDebitFullType() throws HttpCommunicationException {
		SepaDirectDebit sddOriginal = getSepaDirectDebit();
		SepaDirectDebit sddCreated = getHeidelpay().createPaymentType(sddOriginal);
		assertSddEquals(sddOriginal, sddCreated);
	}

	@Test
	public void testChargeSepaDirectDebitType() throws HttpCommunicationException, MalformedURLException {
		SepaDirectDebit sdd = getHeidelpay().createPaymentType(getSepaDirectDebit());
		Charge charge = sdd.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void testFetchSepaDirectDebitType() throws HttpCommunicationException {
		SepaDirectDebit sdd = getHeidelpay().createPaymentType(getSepaDirectDebit());
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
