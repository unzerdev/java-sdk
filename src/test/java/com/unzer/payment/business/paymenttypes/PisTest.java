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

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Pis;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.Assert.assertNotNull;

public class PisTest extends AbstractPaymentTest {

	@Test
	public void testCreatePis() throws HttpCommunicationException {
		Pis pis = new Pis();
		pis = getUnzer().createPaymentType(pis);
		assertNotNull(pis.getId());
	}

	@Test
	public void testCreatePisWithIbanBic() throws HttpCommunicationException {
		Pis pis = new Pis("DE69545100670661762678", "SPFKAT2BXXX");
		pis = getUnzer().createPaymentType(pis);
		assertNotNull(pis.getId());
		assertNotNull(pis.getIban());
		assertNotNull(pis.getBic());

		Pis fetchedPis = (Pis) getUnzer().fetchPaymentType(pis.getId());
		assertNotNull(fetchedPis.getId());
		assertNotNull(fetchedPis.getIban());
		assertNotNull(fetchedPis.getBic());
	}

	@Test
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Pis pis = getUnzer().createPaymentType(new Pis());
		Charge charge = pis.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPisType() throws HttpCommunicationException {
		Pis pis = getUnzer().createPaymentType(new Pis());
		assertNotNull(pis.getId());
		Pis fetchedPis = (Pis) getUnzer().fetchPaymentType(pis.getId());
		assertNotNull(fetchedPis.getId());
	}

	@Test
	@Ignore("AHC-3615 PIS holder not in response when doing a POST/GET")
	public void testFetchPisTypeWithHolderBicIban() throws HttpCommunicationException {
		Pis fetchedPis = (Pis) getUnzer().fetchPaymentType("s-pis-ivt4ibypi0zk");
		assertNotNull(fetchedPis.getId());
		assertNotNull(fetchedPis.getIban());
		assertNotNull(fetchedPis.getBic());
		assertNotNull(fetchedPis.getHolder());
	}
}
