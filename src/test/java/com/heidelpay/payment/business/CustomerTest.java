package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.junit.Test;

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
	// TODO How to update a customer? Should we remove restriction of Constructor?
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
	
	@Test(expected=PaymentException.class)
	public void testDeleteCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		getHeidelpay().deleteCustomer(customer.getId());
		getHeidelpay().fetchCustomer(customer.getId());
	}

}
