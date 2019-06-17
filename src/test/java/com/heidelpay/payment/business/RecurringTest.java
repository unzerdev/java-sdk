package com.heidelpay.payment.business;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.junit.Test;

import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Metadata;
import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class RecurringTest extends AbstractPaymentTest {

	@Test
	public void testRecurringCardWithoutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
		Recurring recurring = getHeidelpay().recurring(createPaymentTypeCard().getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
	}

	@Test
	public void testRecurringCardWitCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Recurring recurring = getHeidelpay().recurring(createPaymentTypeCard().getId(), customer.getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
	}

	@Test
	public void testRecurringCardWitCustomerWithCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Recurring recurring = getHeidelpay().recurring(createPaymentTypeCard().getId(), customer.getCustomerId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
	}

	@Test
	public void testRecurringCardWitCustomerAndMetadata() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		Metadata metadata = getHeidelpay().createMetadata(getTestMetadata());
		Recurring recurring = getHeidelpay().recurring(createPaymentTypeCard().getId(), customer.getId(), metadata.getId(), new URL("https://www.heidelpay.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
	}

	private void assertRecurring(Recurring recurring, Recurring.Status status) {
		assertNotNull(recurring);
		assertNotNull(recurring.getProcessing());
		assertNotNull(recurring.getProcessing().getUniqueId());
		assertNotNull(recurring.getDate());
		
		assertNotNull(recurring.getHeidelpay());
		assertNotNull(recurring.getDate());
		assertNotNull(recurring.getRedirectUrl());
		assertEquals(status, recurring.getStatus());
	}

}
