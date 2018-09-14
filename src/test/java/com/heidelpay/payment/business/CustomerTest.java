package com.heidelpay.payment.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.junit.Test;

import com.heidelpay.payment.Customer;
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
	
	@Test(expected=HttpCommunicationException.class)
	public void testDeleteCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		getHeidelpay().deleteCustomer(customer.getId());
		getHeidelpay().fetchCustomer(customer.getId());
	}

}
