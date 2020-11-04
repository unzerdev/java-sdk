package com.unzer.payment.business;

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

import com.unzer.payment.Customer;
import com.unzer.payment.CustomerCompanyData;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BusinessCustomerTest extends AbstractPaymentTest {

	
	@Test
	public void testCreateRegisteredBusinessCustomer() throws HttpCommunicationException {
		Customer customer = getUnzer().createCustomer(getRegisterdMinimumBusinessCustomer());
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(getRegisterdMinimumBusinessCustomer(), customer);
	}

	@Test
	public void testCreateRegisteredMaximumBusinessCustomer() throws HttpCommunicationException, ParseException {
		Customer maxCustomer = getRegisterdMaximumBusinessCustomer(getRandomId());
		Customer customer = getUnzer().createCustomer(maxCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(maxCustomer, customer);
	}
	
	@Test
	public void testFetchRegisteredMaximumBusinessCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getRegisterdMaximumBusinessCustomer(getRandomId());
		customer = getUnzer().createCustomer(customer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		
		Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
		assertCustomerEquals(customer, fetchedCustomer);
	}

	@Test
	@Ignore("Bug https://heidelpay.atlassian.net/browse/AHC-1644")
	public void testCreateUnRegisteredBusinessCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getUnRegisterdMinimumBusinessCustomer());
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(getRegisterdMinimumBusinessCustomer(), customer);
	}

	@Test
	@Ignore("Bug https://heidelpay.atlassian.net/browse/AHC-1644")
	public void testCreateUnRegisteredMaximumBusinessCustomer() throws HttpCommunicationException, ParseException {
		Customer maxCustomer = getUnRegisterdMaximumBusinessCustomer();
		Customer customer = getUnzer().createCustomer(maxCustomer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		assertCustomerEquals(maxCustomer, customer);
	}
	
	@Test
	@Ignore("Bug https://heidelpay.atlassian.net/browse/AHC-1644")
	public void testFetchUnRegisteredMaximumBusinessCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getUnRegisterdMaximumBusinessCustomer();
		customer = getUnzer().createCustomer(customer);
		assertNotNull(customer);
		assertNotNull(customer.getId());
		
		Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
		assertCustomerEquals(customer, fetchedCustomer);
	}

	@Test
	public void testUpdateCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getRegisterdMaximumBusinessCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		Customer customerToUpdate = new Customer("FelderIT GmbH");
		CustomerCompanyData company = getRegisteredCompanyData();
		company.setCommercialRegisterNumber("ABC993448 DÜSSELDORF");
		customerToUpdate.setCompanyData(company);
		Customer updatedCustomer = getUnzer().updateCustomer(customer.getId(), customerToUpdate);
		assertEquals("FelderIT GmbH", updatedCustomer.getCompany());
		Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
		assertEquals("FelderIT GmbH", fetchedCustomer.getCompany());
		assertEquals("ABC993448 DÜSSELDORF", fetchedCustomer.getCompanyData().getCommercialRegisterNumber());
	}
	
	@Test(expected=PaymentException.class)
	public void testDeleteCustomer() throws HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getRegisterdMaximumBusinessCustomer(getRandomId()));
		assertNotNull(customer);
		assertNotNull(customer.getId());
		getUnzer().deleteCustomer(customer.getId());
		getUnzer().fetchCustomer(customer.getId());
	}

}
