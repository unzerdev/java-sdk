package com.heidelpay.payment.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.junit.Test;

import com.heidelpay.payment.Address;
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

	private void assertCustomerEquals(Customer customerExpected, Customer customer) {
		assertEquals(customerExpected.getFirstname(), customer.getFirstname());
		assertEquals(customerExpected.getLastname(), customer.getLastname());
		assertEquals(customerExpected.getCustomerId(), customer.getCustomerId());
		// Deactivated until Bug  AHC-268 is fixed
//		assertEquals(customerExpected.getBirthDate(), customer.getBirthDate());
		assertEquals(customerExpected.getEmail(), customer.getEmail());
		assertEquals(customerExpected.getMobile(), customer.getMobile());
		assertEquals(customerExpected.getPhone(), customer.getPhone());
		assertAddressEquals(customerExpected.getAddress(), customer.getAddress());		
	}
	private void assertAddressEquals(Address addressExpected, Address address) {
		if (addressExpected == null) return;
		assertEquals(addressExpected.getCity(), address.getCity());
		assertEquals(addressExpected.getCountry(), address.getCountry());
		assertEquals(addressExpected.getName(), address.getName());
		assertEquals(addressExpected.getState(), address.getState());
		assertEquals(addressExpected.getStreet(), address.getStreet());
		assertEquals(addressExpected.getZip(), address.getZip());
	}

}
