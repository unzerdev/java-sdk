package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.Basket;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
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

public class SepaDirectDebitSecuredTest extends AbstractPaymentTest {

	@Test
	public void testCreateSepaDirectDebitSecuredManatoryType() throws HttpCommunicationException {
		SepaDirectDebitSecured sdd = new SepaDirectDebitSecured("DE89370400440532013000");
		sdd = getUnzer().createPaymentType(sdd);
		assertNotNull(sdd.getId());
	}

	@Test
	public void testCreateSepaDirectDebitSecuredFullType() throws HttpCommunicationException {
		SepaDirectDebitSecured sddOriginal = getSepaDirectDebitSecured();
		SepaDirectDebitSecured sddCreated = getUnzer().createPaymentType(sddOriginal);
		assertSddEquals(sddOriginal, sddCreated);
	}

	@Test
	public void testFetchSepaDirectDebitSecuredType() throws HttpCommunicationException {
		SepaDirectDebitSecured sdd = getUnzer().createPaymentType(getSepaDirectDebitSecured());
		assertNotNull(sdd.getId());
		SepaDirectDebitSecured fetchedSdd = (SepaDirectDebitSecured) getUnzer().fetchPaymentType(sdd.getId());
		assertNotNull(fetchedSdd.getId());
		assertSddEquals(sdd, fetchedSdd);
	}

	@Test
	public void testChargeSepaDirectDebitSecuredType() throws HttpCommunicationException, MalformedURLException, ParseException {
		SepaDirectDebitSecured sdd = getUnzer().createPaymentType(getSepaDirectDebitSecured());
		assertNotNull(sdd.getId());
		Basket basket = getUnzer().createBasket(getMinTestBasket());
		sdd.charge(basket.getAmountTotalGross(), Currency.getInstance("EUR"), new URL("https://www.unzer.com"), getMaximumCustomerSameAddress(getRandomId()), basket);
	}
	
	private void assertSddEquals(SepaDirectDebitSecured sddOriginal, SepaDirectDebitSecured sddCreated) {
		assertNotNull(sddCreated.getId());
		assertEquals(sddOriginal.getBic(), sddCreated.getBic());
		assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
		assertEquals(sddOriginal.getIban(), sddCreated.getIban());
	}


	private SepaDirectDebitSecured getSepaDirectDebitSecured() {
		SepaDirectDebitSecured sdd = new SepaDirectDebitSecured("DE89370400440532013000");
		sdd.setBic("COBADEFFXXX");
		sdd.setHolder("Max Mustermann");
		return sdd;
	}

}