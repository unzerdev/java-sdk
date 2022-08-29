package com.unzer.payment.business;


import com.unzer.payment.Customer;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;

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
}
