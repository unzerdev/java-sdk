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

import com.heidelpay.payment.Basket;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.InvoiceFactoring;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import org.junit.Test;

/**
 * @deprecated @deprecated use {@code InvoiceSecuredTest} as a default implementation.
 */
@Deprecated
public class InvoiceFactoringTest extends AbstractPaymentTest {

	@Test
	public void testCreateInvoiceFactoringManatoryType() throws HttpCommunicationException {
		InvoiceFactoring invoice = getHeidelpay().createPaymentType(getInvoiceFactoring());
		assertNotNull(invoice.getId());
	}

	@Test
	public void testChargeType() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceFactoring invoice = createInvoiceFactoring();
		Basket basket =  getTestBasketForInvoice();
		Charge charge = invoice.charge(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomInvoiceId()), basket);
		assertNotNull(charge);
		assertNotNull(charge.getPaymentId());
	}

	@Test
	public void testChargeTypeWithInvoiceId() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceFactoring invoice = createInvoiceFactoring();
		Basket basket =  getTestBasketForInvoice();
		Charge charge = invoice.charge(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getFactoringOKCustomer(getRandomInvoiceId()), basket, getRandomInvoiceId());
		assertNotNull(charge);
		assertNotNull(charge.getPaymentId());
		getHeidelpay().shipment(charge.getPaymentId());
	}

	private InvoiceFactoring createInvoiceFactoring() throws HttpCommunicationException {
		return getHeidelpay().createPaymentType(getInvoiceFactoring());
	}

	@Test(expected=PaymentException.class)
	public void testChargeTypeDifferentAddresses() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceFactoring invoice = createInvoiceFactoring();
		invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(getRandomInvoiceId()), getMaxTestBasket());		
	}

	@Test
	public void testShipmentInvoiceFactoringTypeWithInvoiceId() throws HttpCommunicationException, MalformedURLException, ParseException {
		Basket basket = getHeidelpay().createBasket(getTestBasketForInvoice());
		assertNotNull(basket);
		Charge charge = getHeidelpay().charge(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()), Currency.getInstance("EUR"), createInvoiceFactoring().getId(), new URL("https://www.meinShop.de"), createFactoringOKCustomer().getId() , basket.getId(), false);
		Shipment shipment = getHeidelpay().shipment(charge.getPaymentId(), getRandomInvoiceId(), "3451");
		assertNotNull(shipment);
		assertNotNull(shipment.getId());
		assertNotNull(shipment.getInvoiceId());
		assertNotNull(shipment.getOrderId());
	}

	@Test
	public void testFetchInvoiceFactoringType() throws HttpCommunicationException {
		InvoiceFactoring invoice = createInvoiceFactoring();
		assertNotNull(invoice.getId());
		InvoiceFactoring fetchedInvoiceFactoring = (InvoiceFactoring) getHeidelpay().fetchPaymentType(invoice.getId());
		assertNotNull(fetchedInvoiceFactoring.getId());
	}

	
	private InvoiceFactoring getInvoiceFactoring() {
		return new InvoiceFactoring();
	}

}
