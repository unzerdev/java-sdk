package com.unzer.payment.business.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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

import com.unzer.payment.business.AbstractPaymentTest;
import org.junit.Test;

import com.unzer.payment.Charge;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Sofort;

public class SofortTest extends AbstractPaymentTest {

	@Test
	public void testCreateSofortManatoryType() throws HttpCommunicationException {
		Sofort sofort = new Sofort();
		sofort = getUnzer().createPaymentType(sofort);
		assertNotNull(sofort.getId());
	}

	@Test
	public void testChargeSofortType() throws HttpCommunicationException, MalformedURLException {
		Sofort sofort = getUnzer().createPaymentType(getSofort());
		Charge charge = sofort.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchSofortType() throws HttpCommunicationException {
		Sofort sofort = getUnzer().createPaymentType(getSofort());
		assertNotNull(sofort.getId());
		Sofort fetchedSofort = (Sofort) getUnzer().fetchPaymentType(sofort.getId());
		assertNotNull(fetchedSofort.getId());
	}

	
	private Sofort getSofort() {
		Sofort sofort = new Sofort();
		return sofort;
	}


}
