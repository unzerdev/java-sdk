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
import static org.junit.Assert.assertThrows;

import java.text.ParseException;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import com.heidelpay.payment.Customer;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class CustomerTest extends AbstractPaymentTest {

	@Test
	public void testCreateMinimumCustomer() throws HttpCommunicationException {
		Customer customer = getHeidelpay().createCustomer(getMinimumCustomer());
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertEquals(getMinimumCustomer().getFirstname(), customer.getFirstname());
		assertEquals(getMinimumCustomer().getLastname(), customer.getLastname());
	}

	@Test
	public void testCreateMaximumCustomer() throws HttpCommunicationException, ParseException {
		Customer maxCustomer = getMaximumCustomer(getRandomId());
		Customer customer = getHeidelpay().createCustomer(maxCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(maxCustomer, customer);
	}

	@Test
	public void testCreateMaximumMrsCustomer() throws HttpCommunicationException, ParseException {
		Customer maxMrsCustomer = getMaximumMrsCustomer(getRandomId());
		Customer customer = getHeidelpay().createCustomer(maxMrsCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(maxMrsCustomer, customer);
	}

	@Test
	public void testCreateMaximumUnknownCustomer() throws HttpCommunicationException, ParseException {
		Customer unknownMrsCustomer = getMaximumUnknownCustomer(getRandomId());
		Customer customer = getHeidelpay().createCustomer(unknownMrsCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(unknownMrsCustomer, customer);
	}
	
	@Test
	public void testFetchCustomerMinimum() throws HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMinimumCustomer());
		assertNotNull(customer);
		assertNotNull(customer.getId());
		
		Customer fetchedCustomer = getHeidelpay().fetchCustomer(customer.getId());
		assertCustomerEquals(customer, fetchedCustomer);
	}

	@Test
	public void testFetchCustomerMaximum() throws HttpCommunicationException, ParseException {
		Customer expectedCustomer = getMaximumCustomer(getRandomId());
		Customer customer = getHeidelpay().createCustomer(expectedCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		
		Customer fetchedCustomer = getHeidelpay().fetchCustomer(customer.getId());
		assertCustomerEquals(expectedCustomer, fetchedCustomer);
	}

	@Test
	public void testUpdateCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		Customer customerToUpdate = new Customer(customer.getFirstname(), customer.getLastname());
		customerToUpdate.setFirstname("Max");
		Customer updatedCustomer = getHeidelpay().updateCustomer(customer.getId(), customerToUpdate);
		assertEquals("Max", updatedCustomer.getFirstname());
		Customer fetchedCustomer = getHeidelpay().fetchCustomer(customer.getId());
		assertEquals("Max", fetchedCustomer.getFirstname());
	}
	
	@Test
	public void testDeleteCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		String deletedCustomerId = getHeidelpay().deleteCustomer(customer.getId());
		assertEquals(customer.getId(), deletedCustomerId);
	}

	@Test
	public void testCreateCustomerWithException() throws HttpCommunicationException, ParseException {
		ThrowingRunnable createNullCustomer = new ThrowingRunnable() {
			@Override
			public void run() throws Throwable {
				getHeidelpay().createCustomer(null);
			}
		};
		
		ThrowingRunnable createExistedCustomer = new ThrowingRunnable() {
			@Override
			public void run() throws Throwable {
				Customer customerRequest = new Customer("User", "Test");
				customerRequest.setId("s-cst-abcdef");
				getHeidelpay().createCustomer(customerRequest);
			}
		};
		assertThrows(IllegalArgumentException.class, createNullCustomer);
		assertThrows(PaymentException.class, createExistedCustomer);
	}
}
