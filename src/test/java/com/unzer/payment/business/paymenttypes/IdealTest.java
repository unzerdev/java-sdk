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

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Ideal;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.Assert.assertNotNull;

public class IdealTest extends AbstractPaymentTest {

	@Test
	public void testCreateIdealManatoryType() throws HttpCommunicationException {
		Ideal ideal = (Ideal) getUnzer().createPaymentType(getIdeal());
		assertNotNull(ideal.getId());
	}

	@Test
	public void testChargeIdealType() throws HttpCommunicationException, MalformedURLException {
		Ideal ideal = getUnzer().createPaymentType(getIdeal());
		Charge charge = ideal.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchIdealType() throws HttpCommunicationException {
		Ideal ideal = getUnzer().createPaymentType(getIdeal());
		assertNotNull(ideal.getId());
		Ideal fetchedIdeal = (Ideal) getUnzer().fetchPaymentType(ideal.getId());
		assertNotNull(fetchedIdeal.getId());
	}

	
	private Ideal getIdeal() {
		Ideal ideal = new Ideal().setBic("RABONL2U");
		return ideal;
	}


}
