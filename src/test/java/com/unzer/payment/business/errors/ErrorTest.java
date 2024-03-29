package com.unzer.payment.business.errors;


import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Card;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorTest extends AbstractPaymentTest {
    @Test
    public void testKeyMissing() {
        assertThrows(
                AssertionError.class,
                () -> new Unzer(null)
        );

        assertThrows(
                AssertionError.class,
                () -> new Unzer("")
        );
    }

    // The given key something is unknown or invalid.
    // The key 's-priv-123' is invalid
    @Test
    public void testInvalidKey() throws HttpCommunicationException {
        try {
            getUnzer("s-priv-123").authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.000.002", e.getPaymentErrorList().get(0).getCode());
            assertEquals("The given key s-p****123 is unknown or invalid.",
                    e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    // You do not have the permission to access this resource. This might happen you
    // are using either invalid credentials
    // Card resources can only be created directly with a valid PCI certification.
    // Please contact Unzer to grant permission for PCI level SAQ-D or SAQ-A EP
    @Test
    public void testPCILevelSaqA() {
        try {
            getUnzer(Keys.PUBLIC_KEY).createPaymentType(getPaymentTypeCard()); // Prod Sandbox
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.710.000.003", e.getPaymentErrorList().get(0).getCode());
            assertEquals(
                    "You do not have permission to access this resource. Please contact the owner of the shop.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    // You do not have the permission to access the paymentmethod with the id
    // s-crd-jy8xfchnfte2.
    // Payment type '/types/s-crd-jbrjthrghag2' not found
    //FIXME tests nothing
    @Test
    public void testInvalidAccess() {
        Card card = createPaymentTypeCard(getUnzer(), "4711100000000000");
        try {
            getUnzer(Keys.DEFAULT).fetchPaymentType(card.getId());  // Prod-Sandbox

        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.500.100.001", e.getPaymentErrorList().get(0).getCode());
            assertEquals("The given payment method " + card.getId() + " is not found.",
                    e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    // Invalid return URL
    // Return URL is mandatory
    @Test
    public void testMissingReturnUrl() throws HttpCommunicationException {
        try {
            Authorization authorization = getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId());
            authorization.setReturnUrl(null);
            getUnzer().authorize(authorization);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.100.203", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Return URL is missing.", e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    @Test
    public void testPaymentTypeIdInvalid() throws HttpCommunicationException {
        try {
            getUnzer().authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.200.143", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Resources: type id is missing.", e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    @Test
    public void testFetchNonExistingPayment() {
        try {
            getUnzer().fetchAuthorization("213");
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.310.100.003", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Payment not found with key 213", e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    @Test
    public void testFetchNonExistingCharge() throws HttpCommunicationException {
        Charge charge = getUnzer().charge(getCharge());
        Charge chargeFetched = getUnzer().fetchCharge(charge.getPaymentId(), "s-chg-200");
        assertNull(chargeFetched);
    }

    @Test
    public void testinvalidPutCustomer() throws HttpCommunicationException, ParseException {
        try {
            Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
            Customer customerUpdate = new Customer(customer.getFirstname(), customer.getLastname());
            customerUpdate.setEmail("max");
            getUnzer().updateCustomer(customer.getId(), customerUpdate);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("email has invalid format.", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
        }
    }

    @Test
    public void testCreateInvalidCustomer() throws HttpCommunicationException, ParseException {
        try {
            Customer customer = getMinimumCustomer();
            customer.setBirthDate(getDate("01.01.1944"));
            customer.setEmail("max");
            customer.setMobile("xxx");
            customer.setFirstname("This is a very long first name because someone put the wrong content into the field");
            customer.setLastname("This is a very long last name because someone put the wrong content into the field");
            getUnzer().createCustomer(customer);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.410.200.005", getCode("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("firstName has invalid length.", getMerchantMessage("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("API.410.200.002", getCode("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("lastName has invalid length.", getMerchantMessage("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("email has invalid format.", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
        }
    }

    private String getCode(String value, List<PaymentError> paymentErrorList) {
        for (PaymentError paymentError : paymentErrorList) {
            if (value.equals(paymentError.getCode())) return paymentError.getCode();
        }
        return null;
    }

    private String getMerchantMessage(String value, List<PaymentError> paymentErrorList) {
        for (PaymentError paymentError : paymentErrorList) {
            if (value.equals(paymentError.getCode())) return paymentError.getMerchantMessage();
        }
        return null;
    }

}
