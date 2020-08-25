package com.heidelpay.payment.business.paymenttypes;

import static org.junit.Assert.assertEquals;

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
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Paypal;

public class PaypalTest extends AbstractPaymentTest {

	@Test
	public void testCreatePaypalMandatoryType() throws HttpCommunicationException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getHeidelpay().createPaymentType(paypal);
		assertNotNull(paypal.getId());
	}
	
	@Test
	public void testCreatePaypalWithEmail() throws HttpCommunicationException {
		Paypal paypal = new Paypal();
		paypal.setEmail("test.user@email.com");
		paypal = (Paypal) getHeidelpay().createPaymentType(paypal);
		assertNotNull(paypal.getId());
		assertEquals("test.user@email.com", paypal.getEmail());
	}

	@Test
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		Authorization authorization = paypal.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
		assertNotNull(authorization);
	}

	@Test
	public void testChargePaypalType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		Charge charge = paypal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPaypalType() throws HttpCommunicationException {
		Paypal paypal = getHeidelpay().createPaymentType(getPaypal());
		assertNotNull(paypal.getId());
		Paypal fetchedPaypal = (Paypal) getHeidelpay().fetchPaymentType(paypal.getId());
		assertNotNull(fetchedPaypal.getId());
	}

	private Paypal getPaypal() {
		Paypal paypal = new Paypal();
		return paypal;
	}

}
