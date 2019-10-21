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

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.heidelpay.payment.Basket;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;

public class InvoiceGuaranteedTest extends AbstractPaymentTest {

	@Test
	public void testChargeTypeWithInvoiceId()
			throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceGuaranteed invoice = getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		Basket basket = getMinTestBasket();
		String invoiceId = getRandomInvoiceId();
		Charge charge = invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"),
				new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), basket,
				invoiceId);
		assertNotNull(charge);
		assertNotNull(charge.getPaymentId());
		assertEquals(invoiceId, charge.getInvoiceId());
	}

	@Test
	public void testCreateInvoiceGuaranteedManatoryType() throws HttpCommunicationException {
		InvoiceGuaranteed invoice = getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		assertNotNull(invoice.getId());
	}

	@Test
	public void testChargeType() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceGuaranteed invoice = getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()));		
	}

	@Test(expected=PaymentException.class)
	public void testChargeTypeDifferentAddresses() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceGuaranteed invoice = getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(getRandomId()));		
	}

	@Test
	public void testShipmentInvoiceGuaranteedType() throws HttpCommunicationException, MalformedURLException, ParseException {
		Charge charge = getHeidelpay().charge(BigDecimal.TEN, Currency.getInstance("EUR"), new InvoiceGuaranteed(), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()));
		String invoiceId = new Date().getTime() + "";
		Shipment shipment = getHeidelpay().shipment(charge.getPaymentId(), invoiceId);
		assertNotNull(shipment);
		assertNotNull(shipment.getId());
		assertEquals(invoiceId, shipment.getInvoiceId());
	}

	@Test
	public void testFetchInvoiceGuaranteedType() throws HttpCommunicationException {
		InvoiceGuaranteed invoice = getHeidelpay().createPaymentType(getInvoiceGuaranteed());
		assertNotNull(invoice.getId());
		InvoiceGuaranteed fetchedInvoiceGuaranteed = (InvoiceGuaranteed) getHeidelpay().fetchPaymentType(invoice.getId());
		assertNotNull(fetchedInvoiceGuaranteed.getId());
	}

	
	private InvoiceGuaranteed getInvoiceGuaranteed() {
		return new InvoiceGuaranteed();
	}

}
