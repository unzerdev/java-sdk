package com.unzer.payment.business;


import com.unzer.payment.Customer;
import com.unzer.payment.CustomerV2;
import com.unzer.payment.PaymentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerV2Test extends AbstractPaymentTest {
    @Test
    void testCreateMinimumCustomer() {
        Customer customer = getUnzer().createCustomer(new CustomerV2("firstname", "lastname"));
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
    void testDeleteCustomer() {
        Customer customer = getUnzer().createCustomer(initV2Customer());
        assertNotNull(customer);
        assertNotNull(customer.getId());
        String deletedCustomerId = getUnzer().deleteCustomer(customer.getId());
        assertEquals(customer.getId(), deletedCustomerId);
    }

    @Test
    void testCreateCustomerWithException() {
        assertThrows(IllegalArgumentException.class, () -> {
            getUnzer().createCustomer(null);
        });
        assertThrows(PaymentException.class, () -> {
            Customer customerRequest = new CustomerV2("User", "Test");
            customerRequest.setId("s-cst-abcdef");
            getUnzer().createCustomer(customerRequest);
        });
    }
}
