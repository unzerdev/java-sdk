/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.business;


import com.unzer.payment.Customer;
import com.unzer.payment.PaymentException;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.text.ParseException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

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
        Customer maxCustomer = getMaximumCustomer(generateUuid());
        Customer customer = getUnzer().createCustomer(maxCustomer);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertCustomerEquals(maxCustomer, customer);
    }

    @Test
    public void testCreateMaximumMrsCustomer() throws HttpCommunicationException, ParseException {
        Customer maxMrsCustomer = getMaximumMrsCustomer(generateUuid());
        Customer customer = getUnzer().createCustomer(maxMrsCustomer);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertCustomerEquals(maxMrsCustomer, customer);
    }

    @Test
    public void testCreateMaximumUnknownCustomer() throws HttpCommunicationException, ParseException {
        Customer unknownMrsCustomer = getMaximumUnknownCustomer(generateUuid());
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
        Customer expectedCustomer = getMaximumCustomer(generateUuid());
        Customer customer = getUnzer().createCustomer(expectedCustomer);
        assertNotNull(customer);
        assertNotNull(customer.getId());

        Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
        assertCustomerEquals(expectedCustomer, fetchedCustomer);
    }

    @Test
    public void testUpdateCustomer() throws HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
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
        Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
        assertNotNull(customer);
        assertNotNull(customer.getId());
        String deletedCustomerId = getUnzer().deleteCustomer(customer.getId());
        assertEquals(customer.getId(), deletedCustomerId);
    }

    @Test
    public void testCreateCustomerWithException() {
        assertThrows(IllegalArgumentException.class, () -> {
            getUnzer().createCustomer(null);
        });
        assertThrows(PaymentException.class, () -> {
            Customer customerRequest = new Customer("User", "Test");
            customerRequest.setId("s-cst-abcdef");
            getUnzer().createCustomer(customerRequest);
        });
    }

    @TestFactory
    public Collection<DynamicTest> testCustomerSalutation() {
        class TestCase {
            final Customer customer;
            final Customer.Salutation expectedSalutation;

            public TestCase(Customer customer, Customer.Salutation expectedSalutation) {
                this.customer = customer;
                this.expectedSalutation = expectedSalutation;
            }
        }
        return Stream.of(
                new TestCase(new Customer("Abc", "Def"), Customer.Salutation.UNKNOWN),
                new TestCase(new Customer("Abc", "Def").setSalutation(null), Customer.Salutation.UNKNOWN),
                new TestCase(new Customer("Abc", "Def").setSalutation(Customer.Salutation.MR), Customer.Salutation.MR),
                new TestCase(new Customer("Abc", "Def").setSalutation(Customer.Salutation.MRS), Customer.Salutation.MRS),
                new TestCase(new Customer("Abc", "Def").setSalutation(Customer.Salutation.UNKNOWN), Customer.Salutation.UNKNOWN)

        ).map(tc -> dynamicTest("salutation " + tc.expectedSalutation, () -> {
            Unzer unzer = getUnzer();

            Customer createdCustomer = unzer.createCustomer(tc.customer);
            assertEquals(tc.expectedSalutation, createdCustomer.getSalutation());

            Customer fetchedCustomer = unzer.fetchCustomer(createdCustomer.getId());
            assertEquals(tc.expectedSalutation, fetchedCustomer.getSalutation());
        })).collect(Collectors.toList());
    }
}
