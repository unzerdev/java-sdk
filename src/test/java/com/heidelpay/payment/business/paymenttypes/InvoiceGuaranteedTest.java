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

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;

public class InvoiceGuaranteedTest extends AbstractPaymentTest {

	@Test
	public void testCreateInvoiceGuaranteedManatoryType() throws HttpCommunicationException {
		InvoiceGuaranteed invoice = (InvoiceGuaranteed) getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		assertNotNull(invoice.getId());
	}

	@Test
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceGuaranteed invoice = getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		invoice.authorize(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.mpay24.com"), getMaximumCustomer(getRandomId()));		
	}

	@Test
	public void testShipmentInvoiceGuaranteedType() throws HttpCommunicationException, MalformedURLException, ParseException {
		Authorization authorize = getHeidelpay().authorize(BigDecimal.TEN, Currency.getInstance("EUR"), new InvoiceGuaranteed(), new URL("https://www.google.at"), getMaximumCustomer(getRandomId()));
		Shipment shipment = getHeidelpay().shipment(authorize.getPaymentId());
		assertNotNull(shipment);
		assertNotNull(shipment.getId());
	}

	@Test
	public void testFetchInvoiceGuaranteedType() throws HttpCommunicationException {
		InvoiceGuaranteed invoice = getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		assertNotNull(invoice.getId());
		InvoiceGuaranteed fetchedInvoiceGuaranteed = (InvoiceGuaranteed) getHeidelpay().fetchPaymentType(invoice.getId());
		assertNotNull(fetchedInvoiceGuaranteed.getId());
	}

	
	private InvoiceGuaranteed getInvoiceGuaranteed() {
		InvoiceGuaranteed invoice = new InvoiceGuaranteed();
		return invoice;
	}

}
