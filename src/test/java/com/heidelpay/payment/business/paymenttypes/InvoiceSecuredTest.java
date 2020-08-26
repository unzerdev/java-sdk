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

import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.InvoiceSecured;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InvoiceSecuredTest extends AbstractPaymentTest {

	@Test
	public void testChargeTypeWithInvoiceId()
			throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceSecured invoice = getHeidelpay().createPaymentType(getInvoiceSecured());
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
	public void testCreateInvoiceSecuredManatoryType() throws HttpCommunicationException {
		InvoiceSecured invoice = getHeidelpay().createPaymentType(getInvoiceSecured());
		assertNotNull(invoice.getId());
	}

	@Test
	public void testChargeType() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceSecured invoice = getHeidelpay().createPaymentType(getInvoiceSecured());
		Basket basket = getMinTestBasket();
		Charge chargeResult = invoice.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), basket, invoice.getId());
		assertNotNull(chargeResult);
	}

	@Test(expected=PaymentException.class)
	public void testChargeTypeDifferentAddresses() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceSecured invoice = getHeidelpay().createPaymentType(getInvoiceSecured());
		invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(getRandomId()));		
	}

	@Test
	public void testShipmentInvoiceSecuredType() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceSecured invoice = getHeidelpay().createPaymentType(getInvoiceSecured());
		Basket basket = getMinTestBasket();
		String invoiceId = new Date().getTime() + "";
		Charge charge = getHeidelpay().charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), invoice, new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()), basket);
		Shipment shipment = getHeidelpay().shipment(charge.getPaymentId(), invoiceId);
		assertNotNull(shipment);
		assertNotNull(shipment.getId());
		assertEquals(invoiceId, shipment.getInvoiceId());
	}

	@Test
	public void testFetchInvoiceSecuredType() throws HttpCommunicationException {
		InvoiceSecured invoice = getHeidelpay().createPaymentType(getInvoiceSecured());
		assertNotNull(invoice.getId());
		InvoiceSecured fetchedInvoiceSecured = (InvoiceSecured) getHeidelpay().fetchPaymentType(invoice.getId());
		assertNotNull(fetchedInvoiceSecured.getId());
	}

	
	private InvoiceSecured getInvoiceSecured() {
		return new InvoiceSecured();
	}

}
