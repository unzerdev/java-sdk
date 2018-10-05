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
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Sofort;

public class SofortTest extends AbstractPaymentTest {

	@Test
	public void testCreateSofortManatoryType() throws HttpCommunicationException {
		Sofort sofort = new Sofort();
		sofort = getHeidelpay().createPaymentType(sofort);
		assertNotNull(sofort.getId());
	}

	@Test
	public void testChargeSofortType() throws HttpCommunicationException, MalformedURLException {
		Sofort sofort = getHeidelpay().createPaymentType(getSofort());
		Charge charge = sofort.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchSofortType() throws HttpCommunicationException {
		Sofort sofort = getHeidelpay().createPaymentType(getSofort());
		assertNotNull(sofort.getId());
		Sofort fetchedSofort = (Sofort) getHeidelpay().fetchPaymentType(sofort.getId());
		assertNotNull(fetchedSofort.getId());
	}

	
	private Sofort getSofort() {
		Sofort sofort = new Sofort();
		return sofort;
	}


}
