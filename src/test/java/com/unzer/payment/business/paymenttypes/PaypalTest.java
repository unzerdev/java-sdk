package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Paypal;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class PaypalTest extends AbstractPaymentTest {

	@Test
	public void testCreatePaypalMandatoryType() throws HttpCommunicationException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getUnzer().createPaymentType(paypal);
		assertNotNull(paypal.getId());
	}
	
	@Test
	public void testCreatePaypalWithEmail() throws HttpCommunicationException {
		Paypal paypal = new Paypal();
		paypal.setEmail("test.user@email.com");
		paypal = (Paypal) getUnzer().createPaymentType(paypal);
		assertNotNull(paypal.getId());
		assertEquals("test.user@email.com", paypal.getEmail());
	}

	@Test
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getUnzer().createPaymentType(getPaypal());
		Authorization authorization = paypal.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
		assertNotNull(authorization);
	}

	@Test
	public void testChargePaypalType() throws HttpCommunicationException, MalformedURLException {
		Paypal paypal = getUnzer().createPaymentType(getPaypal());
		Charge charge = paypal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPaypalType() throws HttpCommunicationException {
		Paypal paypal = getUnzer().createPaymentType(getPaypal());
		assertNotNull(paypal.getId());
		Paypal fetchedPaypal = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
		assertNotNull(fetchedPaypal.getId());
	}

	private Paypal getPaypal() {
		Paypal paypal = new Paypal();
		return paypal;
	}

}
