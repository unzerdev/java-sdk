package com.unzer.payment.business;


import com.unzer.payment.Customer;
import com.unzer.payment.CustomerV2;
import com.unzer.payment.PaymentException;
import com.unzer.payment.Unzer;
import com.unzer.payment.integration.resources.BearerAuthBaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerV2Test extends BearerAuthBaseTest {
    @Test
    void testCreateMinimumCustomer() {
        Customer customer = getUnzer().createCustomer(new CustomerV2("Max", "Mustermann"));
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertEquals(getMinimumCustomer().getFirstname(), customer.getFirstname());
        assertEquals(getMinimumCustomer().getLastname(), customer.getLastname());
    }

    @Test
    void hashesOfDifferentCustomerVersionsDiffer() {
        Customer customerV1 = new Customer("Max", "Mustermann");
        CustomerV2 customerV2 = new CustomerV2("Max", "Mustermann");

        assertNotEquals(customerV1, customerV2);
        assertNotEquals(customerV1.hashCode(), customerV2.hashCode());
    }

    private static CustomerV2 initV2Customer() {
        return new CustomerV2("firstname", "lastname");
    }

    @Test
    void testFetchCustomerMinimum() {
        Customer customer = getUnzer().createCustomer(initV2Customer());
        assertNotNull(customer);
        assertNotNull(customer.getId());

        Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
        assertCustomerEquals(customer, fetchedCustomer);
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = getUnzer().createCustomer(initV2Customer());
        Customer customerToUpdate = new Customer(customer.getFirstname(), customer.getLastname());
        String updatedName = "Max";
        customerToUpdate.setFirstname(updatedName);
        Customer updatedCustomer = getUnzer().updateCustomer(customer.getId(), customerToUpdate);
        assertEquals(updatedName, updatedCustomer.getFirstname());
        Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
        assertEquals(updatedName, fetchedCustomer.getFirstname());
    }

    @Test
    void testDeleteCustomer() {
        Unzer unzer = getUnzer();
        Customer customer = unzer.createCustomer(initV2Customer());
        String deletedCustomerId = unzer.deleteCustomer(customer.getId());
        assertEquals(customer.getId(), deletedCustomerId);

        assertThrows(PaymentException.class, () -> {
            unzer.fetchCustomer(deletedCustomerId);
        }, "Fetching deleted customer should cause PaymentException");
    }

    @Test
    void testCreateCustomerWithException() {
        Customer customerRequest = new CustomerV2("User", "Test");
        customerRequest.setId("s-cst-abcdef");
        Unzer unzer = getUnzer();
        assertThrows(PaymentException.class, () -> {
            unzer.createCustomer(customerRequest);
        });
    }
}
