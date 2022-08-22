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

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Giropay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GiropayTest extends AbstractPaymentTest {

	@Test
	public void testCreateGiropayManatoryType() throws HttpCommunicationException {
		Giropay giropay = new Giropay();
		giropay = getUnzer().createPaymentType(giropay);
		assertNotNull(giropay.getId());
	}

	@Test
	public void testChargeGiropayType() throws HttpCommunicationException, MalformedURLException {
		Giropay giropay = getUnzer().createPaymentType(getGiropay());
		Charge charge = giropay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchGiropayType() throws HttpCommunicationException {
		Giropay giropay = getUnzer().createPaymentType(getGiropay());
		assertNotNull(giropay.getId());
		Giropay fetchedGiropay = (Giropay) getUnzer().fetchPaymentType(giropay.getId());
		assertNotNull(fetchedGiropay.getId());
	}

	private Giropay getGiropay() {
		return new Giropay();
	}

}
