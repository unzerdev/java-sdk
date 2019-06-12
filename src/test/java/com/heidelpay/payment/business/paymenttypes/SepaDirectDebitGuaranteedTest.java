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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.SepaDirectDebitGuaranteed;

public class SepaDirectDebitGuaranteedTest extends AbstractPaymentTest {

	@Test
	public void testCreateSepaDirectDebitGuaranteedManatoryType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sdd = new SepaDirectDebitGuaranteed("DE89370400440532013000");
		sdd = getHeidelpay().createPaymentType(sdd);
		assertNotNull(sdd.getId());
	}

	@Test
	public void testCreateSepaDirectDebitGuaranteedFullType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sddOriginal = getSepaDirectDebitGuaranteed();
		SepaDirectDebitGuaranteed sddCreated = getHeidelpay().createPaymentType(sddOriginal);
		assertSddEquals(sddOriginal, sddCreated);
	}

	@Test
	public void testFetchSepaDirectDebitGuaranteedType() throws HttpCommunicationException {
		SepaDirectDebitGuaranteed sdd = getHeidelpay().createPaymentType(getSepaDirectDebitGuaranteed());
		assertNotNull(sdd.getId());
		SepaDirectDebitGuaranteed fetchedSdd = (SepaDirectDebitGuaranteed) getHeidelpay().fetchPaymentType(sdd.getId());
		assertNotNull(fetchedSdd.getId());
		assertSddEquals(sdd, fetchedSdd);
	}

	@Test
	public void testChargeSepaDirectDebitGuaranteedType() throws HttpCommunicationException, MalformedURLException, ParseException {
		SepaDirectDebitGuaranteed sdd = getHeidelpay().createPaymentType(getSepaDirectDebitGuaranteed());
		assertNotNull(sdd.getId());
		sdd.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.heidelpay.com"), getMaximumCustomerSameAddress(getRandomId()));
	}
	
	private void assertSddEquals(SepaDirectDebitGuaranteed sddOriginal, SepaDirectDebitGuaranteed sddCreated) {
		assertNotNull(sddCreated.getId());
		assertEquals(sddOriginal.getBic(), sddCreated.getBic());
		assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
		assertEquals(maskIban(sddOriginal.getIban()), sddCreated.getIban());
	}


	private SepaDirectDebitGuaranteed getSepaDirectDebitGuaranteed() {
		SepaDirectDebitGuaranteed sdd = new SepaDirectDebitGuaranteed("DE89370400440532013000");
		sdd.setBic("COBADEFFXXX");
		sdd.setHolder("Rene Felder");
		return sdd;
	}

}
