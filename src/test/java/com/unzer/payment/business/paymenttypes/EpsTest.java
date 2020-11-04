package com.unzer.payment.business.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2018 Unzer GmbH
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
import com.unzer.payment.paymenttypes.Eps;
import org.junit.Test;

import com.unzer.payment.Charge;
import com.unzer.payment.communication.HttpCommunicationException;

public class EpsTest extends AbstractPaymentTest {

	@Test
	public void testCreateEpsWithoutBicType() throws HttpCommunicationException {
		Eps eps = new Eps();
		eps = getUnzer().createPaymentType(eps);
		assertNotNull(eps.getId());
	}

	@Test
	public void testCreateEpsWithBicType() throws HttpCommunicationException {
		Eps eps = new Eps("SPFKAT2BXXX");
		eps = getUnzer().createPaymentType(eps);
		assertNotNull(eps.getId());
	}

	@Test
	public void testChargeEpsType() throws HttpCommunicationException, MalformedURLException {
		Eps eps = getUnzer().createPaymentType(getEps());
		Charge charge = eps.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.google.at"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchEpsType() throws HttpCommunicationException {
		Eps eps = getUnzer().createPaymentType(getEps());
		assertNotNull(eps.getId());
		Eps fetchedEps = (Eps) getUnzer().fetchPaymentType(eps.getId());
		assertNotNull(fetchedEps.getId());
	}

	
	private Eps getEps() {
		Eps eps = new Eps();
		return eps;
	}


}
