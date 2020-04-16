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

import java.math.BigDecimal;
import java.net.MalformedURLException;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class ChargeAfterAuthorizationTest extends AbstractPaymentTest {

	@Test
	public void fetchAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId()));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		assertNotNull(authorization);
	}

	@Test
	public void fullChargeAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		String orderId = getRandomId();
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), null, orderId, null, null, false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Charge charge = authorization.charge();
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals(orderId, authorize.getOrderId());
		assertEquals(orderId, authorization.getOrderId());
		assertEquals(orderId, charge.getOrderId());
	}

	@Test
	public void fullChargeAfterAuthorizationHeidelpay() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Charge charge = getHeidelpay().chargeAuthorization(authorize.getPaymentId());
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void partialChargeAfterAuthorization() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Authorization authorization = getHeidelpay().fetchAuthorization(authorize.getPaymentId());
		Charge charge = authorization.charge(new BigDecimal(0.1));
		assertNotNull(charge);
		assertNotNull(charge.getId());
	}

	@Test
	public void chargeAfterAuthorizationWithPaymentReference() throws HttpCommunicationException, MalformedURLException {
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), false));
		Charge charge = authorize.charge(new BigDecimal(1.0), "pmt-ref");
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertEquals("pmt-ref", charge.getPaymentReference());
	}
}
