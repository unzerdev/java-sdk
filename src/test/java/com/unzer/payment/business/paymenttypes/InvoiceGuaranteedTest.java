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
import com.unzer.payment.paymenttypes.InvoiceGuaranteed;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @deprecated @deprecated use {@code InvoiceSecuredTest} as a default implementation.
 */
@Deprecated
public class InvoiceGuaranteedTest extends AbstractPaymentTest {

	@Test
	public void testChargeTypeWithInvoiceId()
			throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceGuaranteed invoice = getUnzer().createPaymentType(getInvoiceGuaranteed());
		Basket basket = getUnzer().createBasket(getMinTestBasket());
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
		InvoiceGuaranteed invoice = getUnzer().createPaymentType(getInvoiceGuaranteed());
		assertNotNull(invoice.getId());
	}

	@Test
	public void testChargeType() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceGuaranteed invoice = getUnzer().createPaymentType(getInvoiceGuaranteed());
		invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()));		
	}

	@Test(expected=PaymentException.class)
	public void testChargeTypeDifferentAddresses() throws HttpCommunicationException, MalformedURLException, ParseException {
		InvoiceGuaranteed invoice = getUnzer().createPaymentType(getInvoiceGuaranteed());
		invoice.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"), getMaximumCustomer(getRandomId()));		
	}

	@Test
	public void testShipmentInvoiceGuaranteedType() throws HttpCommunicationException, MalformedURLException, ParseException {
		Charge charge = getUnzer().charge(BigDecimal.TEN, Currency.getInstance("EUR"), new InvoiceGuaranteed(), new URL("https://www.meinShop.de"), getMaximumCustomerSameAddress(getRandomId()));
		String invoiceId = new Date().getTime() + "";
		Shipment shipment = getUnzer().shipment(charge.getPaymentId(), invoiceId);
		assertNotNull(shipment);
		assertNotNull(shipment.getId());
		assertEquals(invoiceId, shipment.getInvoiceId());
	}

	@Test
	public void testFetchInvoiceGuaranteedType() throws HttpCommunicationException {
		InvoiceGuaranteed invoice = getUnzer().createPaymentType(getInvoiceGuaranteed());
		assertNotNull(invoice.getId());
		InvoiceGuaranteed fetchedInvoiceGuaranteed = (InvoiceGuaranteed) getUnzer().fetchPaymentType(invoice.getId());
		assertNotNull(fetchedInvoiceGuaranteed.getId());
	}

	
	private InvoiceGuaranteed getInvoiceGuaranteed() {
		return new InvoiceGuaranteed();
	}

}
