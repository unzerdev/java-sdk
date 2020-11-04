package com.unzer.payment.business.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.PaymentException;
import com.unzer.payment.Shipment;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.InvoiceFactoring;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import static org.junit.Assert.assertNotNull;

/**
 * @deprecated @deprecated use {@code InvoiceSecuredTest} as a default implementation.
 */
@Deprecated
public class InvoiceFactoringTest extends AbstractPaymentTest {

	@Test
	public void testCreateInvoiceFactoringManatoryType() throws HttpCommunicationException {
		InvoiceFactoring invoice = getUnzer().createPaymentType(getInvoiceFactoring());
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
		getUnzer().shipment(charge.getPaymentId());
	}

	private InvoiceFactoring createInvoiceFactoring() throws HttpCommunicationException {
		return getUnzer().createPaymentType(getInvoiceFactoring());
	}

	@Test(expected=PaymentException.class)
	public void testChargeTypeDifferentAddresses() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceFactoring invoice = createInvoiceFactoring();
		invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(getRandomInvoiceId()), getMaxTestBasket());		
	}

	@Test
	public void testShipmentInvoiceFactoringTypeWithInvoiceId() throws HttpCommunicationException, MalformedURLException, ParseException {
		Basket basket = getUnzer().createBasket(getTestBasketForInvoice());
		assertNotNull(basket);
		Charge charge = getUnzer().charge(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()), Currency.getInstance("EUR"), createInvoiceFactoring().getId(), new URL("https://www.meinShop.de"), createFactoringOKCustomer().getId() , basket.getId(), false);
		Shipment shipment = getUnzer().shipment(charge.getPaymentId(), getRandomInvoiceId(), "3451");
		assertNotNull(shipment);
		assertNotNull(shipment.getId());
		assertNotNull(shipment.getInvoiceId());
		assertNotNull(shipment.getOrderId());
	}

	@Test
	public void testFetchInvoiceFactoringType() throws HttpCommunicationException {
		InvoiceFactoring invoice = createInvoiceFactoring();
		assertNotNull(invoice.getId());
		InvoiceFactoring fetchedInvoiceFactoring = (InvoiceFactoring) getUnzer().fetchPaymentType(invoice.getId());
		assertNotNull(fetchedInvoiceFactoring.getId());
	}

	
	private InvoiceFactoring getInvoiceFactoring() {
		return new InvoiceFactoring();
	}

}
