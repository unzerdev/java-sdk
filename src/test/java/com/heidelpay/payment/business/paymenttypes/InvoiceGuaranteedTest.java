package com.heidelpay.payment.business.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
