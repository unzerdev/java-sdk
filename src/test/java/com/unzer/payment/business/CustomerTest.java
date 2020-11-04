package com.unzer.payment.business;

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

import com.unzer.payment.Customer;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.text.ParseException;

import static org.junit.Assert.*;

public class CustomerTest extends AbstractPaymentTest {

	@Test
	public void testCreateMinimumCustomer() throws HttpCommunicationException {
		Customer customer = getUnzer().createCustomer(getMinimumCustomer());
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertEquals(getMinimumCustomer().getFirstname(), customer.getFirstname());
		assertEquals(getMinimumCustomer().getLastname(), customer.getLastname());
	}

	@Test
	public void testCreateMaximumCustomer() throws HttpCommunicationException, ParseException {
		Customer maxCustomer = getMaximumCustomer(getRandomId());
		Customer customer = getUnzer().createCustomer(maxCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(maxCustomer, customer);
	}

	@Test
	public void testCreateMaximumMrsCustomer() throws HttpCommunicationException, ParseException {
		Customer maxMrsCustomer = getMaximumMrsCustomer(getRandomId());
		Customer customer = getUnzer().createCustomer(maxMrsCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(maxMrsCustomer, customer);
	}

	@Test
	public void testCreateMaximumUnknownCustomer() throws HttpCommunicationException, ParseException {
		Customer unknownMrsCustomer = getMaximumUnknownCustomer(getRandomId());
		Customer customer = getUnzer().createCustomer(unknownMrsCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(unknownMrsCustomer, customer);
	}
	
	@Test
	public void testFetchCustomerMinimum() throws HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getMinimumCustomer());
		assertNotNull(customer);
		assertNotNull(customer.getId());
		
		Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
		assertCustomerEquals(customer, fetchedCustomer);
	}

	@Test
	public void testFetchCustomerMaximum() throws HttpCommunicationException, ParseException {
		Customer expectedCustomer = getMaximumCustomer(getRandomId());
		Customer customer = getUnzer().createCustomer(expectedCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		
		Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
		assertCustomerEquals(expectedCustomer, fetchedCustomer);
	}

	@Test
	public void testUpdateCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getMaximumCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		Customer customerToUpdate = new Customer(customer.getFirstname(), customer.getLastname());
		customerToUpdate.setFirstname("Max");
		Customer updatedCustomer = getUnzer().updateCustomer(customer.getId(), customerToUpdate);
		assertEquals("Max", updatedCustomer.getFirstname());
		Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
		assertEquals("Max", fetchedCustomer.getFirstname());
	}
	
	@Test
	public void testDeleteCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getMaximumCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		String deletedCustomerId = getUnzer().deleteCustomer(customer.getId());
		assertEquals(customer.getId(), deletedCustomerId);
	}

	@Test
	public void testCreateCustomerWithException() throws HttpCommunicationException, ParseException {
		ThrowingRunnable createNullCustomer = new ThrowingRunnable() {
			@Override
			public void run() throws Throwable {
				getUnzer().createCustomer(null);
			}
		};
		
		ThrowingRunnable createExistedCustomer = new ThrowingRunnable() {
			@Override
			public void run() throws Throwable {
				Customer customerRequest = new Customer("User", "Test");
				customerRequest.setId("s-cst-abcdef");
				getUnzer().createCustomer(customerRequest);
			}
		};
		assertThrows(IllegalArgumentException.class, createNullCustomer);
		assertThrows(PaymentException.class, createExistedCustomer);
	}
}
