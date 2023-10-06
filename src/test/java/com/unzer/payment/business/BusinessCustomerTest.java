package com.unzer.payment.business;


import com.unzer.payment.Customer;
import com.unzer.payment.CustomerCompanyData;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;

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
        Customer maxCustomer = getRegisterdMaximumBusinessCustomer(generateUuid());
        Customer customer = getUnzer().createCustomer(maxCustomer);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertCustomerEquals(maxCustomer, customer);
    }

    @Test
    public void testFetchRegisteredMaximumBusinessCustomer() throws HttpCommunicationException, ParseException {
        Customer customer = getRegisterdMaximumBusinessCustomer(generateUuid());
        customer = getUnzer().createCustomer(customer);
        assertNotNull(customer);
        assertNotNull(customer.getId());

        Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
        assertCustomerEquals(customer, fetchedCustomer);
    }

    @Test
    @Disabled("Bug https://heidelpay.atlassian.net/browse/AHC-1644")
    public void testCreateUnRegisteredBusinessCustomer() throws HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getUnRegisterdMinimumBusinessCustomer());
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertCustomerEquals(getRegisterdMinimumBusinessCustomer(), customer);
    }

    @Test
    @Disabled("Bug https://heidelpay.atlassian.net/browse/AHC-1644")
    public void testCreateUnRegisteredMaximumBusinessCustomer() throws HttpCommunicationException, ParseException {
        Customer maxCustomer = getUnRegisterdMaximumBusinessCustomer();
        Customer customer = getUnzer().createCustomer(maxCustomer);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertCustomerEquals(maxCustomer, customer);
    }

    @Test
    @Disabled("Bug https://heidelpay.atlassian.net/browse/AHC-1644")
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
        Customer customer = getUnzer().createCustomer(getRegisterdMaximumBusinessCustomer(generateUuid()));
        assertNotNull(customer);
        assertNotNull(customer.getId());
        Customer customerToUpdate = new Customer("Unzer E-Com GmbH");
        CustomerCompanyData company = getRegisteredCompanyData();
        company.setCommercialRegisterNumber("ABC993448 DÜSSELDORF");
        customerToUpdate.setCompanyData(company);
        Customer updatedCustomer = getUnzer().updateCustomer(customer.getId(), customerToUpdate);
        assertEquals("Unzer E-Com GmbH", updatedCustomer.getCompany());
        Customer fetchedCustomer = getUnzer().fetchCustomer(customer.getId());
        assertEquals("Unzer E-Com GmbH", fetchedCustomer.getCompany());
        assertEquals("ABC993448 DÜSSELDORF", fetchedCustomer.getCompanyData().getCommercialRegisterNumber());
    }

    @Test
    public void testDeleteCustomer() throws HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getRegisterdMaximumBusinessCustomer(generateUuid()));
        assertNotNull(customer);
        assertNotNull(customer.getId());
        getUnzer().deleteCustomer(customer.getId());
        assertThrows(PaymentException.class, () -> {
            getUnzer().fetchCustomer(customer.getId());
        });
    }

}
