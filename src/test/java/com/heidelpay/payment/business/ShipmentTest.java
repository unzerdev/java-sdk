package com.heidelpay.payment.business;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.junit.Ignore;
import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class ShipmentTest extends AbstractPaymentTest {

	// TODO Problem with Merchant configuration
	@Test
	@Ignore("Merchant is having problem with insurance provider")
	public void testAuthorizeWithShipment() throws MalformedURLException, HttpCommunicationException, ParseException {
		Charge charge = getHeidelpay().charge(getCharge(createPaymentTypeInvoiceGuaranteed().getId(), createMaximumCustomerSameAddress().getId(), null, null, null));
		assertNotNull(charge.getId());
		assertNotNull(charge);
		Shipment shipment = getHeidelpay().shipment(charge.getPaymentId());
		assertNotNull(charge.getId());
		assertNotNull(charge);
		assertNotNull(shipment);
		assertEquals("COR.000.100.112", shipment.getMessage().getCode());
		assertNotNull(shipment.getMessage().getCustomer());
	}

	@Deprecated
	@Test(expected=PaymentException.class)
	public void testAuthorizeWithShipmentNotSameAddress() throws MalformedURLException, HttpCommunicationException, ParseException {
		getHeidelpay().authorize(getAuthorization(createPaymentTypeInvoiceGuaranteed().getId(), createMaximumCustomer().getId()));
	}

	@Test(expected=PaymentException.class)
	public void testAuthorizeWithShipmentNotSameAddressWithInvoiceSecured() throws MalformedURLException, HttpCommunicationException, ParseException {
		getHeidelpay().authorize(getAuthorization(createPaymentTypeInvoiceSecured().getId(), createMaximumCustomer().getId()));
	}

}
