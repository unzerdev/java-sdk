package com.unzer.payment.business.errors;


import com.unzer.payment.Authorization;
import com.unzer.payment.Customer;
import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Card;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorTestDE extends AbstractPaymentTest {

    // The given key something is unknown or invalid.
    // The key 's-priv-123' is invalid
    @Test
    public void testInvalidKey() throws MalformedURLException, HttpCommunicationException {
        try {
            getUnzerDE("s-priv-123").authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.000.002", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Der angegebene key s-p****123 ist unbekannt oder ungültig.",
                    e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Bei der Überprüfung Ihrer Anfrage ist ein Problem aufgetreten. " +
                            "Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    // You do not have the permission to access this resource. This might happen you
    // are using either invalid credentials
    // Card resources can only be created directly with a valid PCI certification.
    // Please contact Unzer to grant permission for PCI level SAQ-D or SAQ-A EP
    @Test
    public void testPCILevelSaqA() {
        try {
            getUnzerDE(Keys.PUBLIC_KEY).createPaymentType(getPaymentTypeCard()); // Prod Sandbox
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.710.000.003", e.getPaymentErrorList().get(0).getCode());
            assertEquals(
                    "Sie sind nicht zum Zugriff auf diese Ressource berechtigt. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    // You do not have the permission to access the paymentmethod with the id
    // s-crd-jy8xfchnfte2.
    // Payment type '/types/s-crd-jbrjthrghag2' not found
    @Test
    public void testInvalidAccess() {
        Card card = createPaymentTypeCard(getUnzer(), "4711100000000000");
        try {
            getUnzerDE(Keys.DEFAULT).fetchPaymentType(card.getId());  // Prod-Sandbox

        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);

            assertEquals("API.710.100.001", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Die Zahlungsmethode " + card.getId() + " existiert nicht.",
                    e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Ungültige Zahlungsmethode. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    // Invalid return URL
    // Return URL is mandatory
    @Test
    public void testMissingReturnUrl() throws MalformedURLException, HttpCommunicationException {
        try {
            Authorization authorization = getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId());
            authorization.setReturnUrl(null);
            getUnzerDE().authorize(authorization);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.100.203", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Die Rückgabe-URL ist ungültig.", e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Rückgabe-URL fehlt. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    @Test
    public void testPaymentTypeIdMissing() throws MalformedURLException, HttpCommunicationException {
        try {
            getUnzerDE().authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.200.143", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Resources type id fehlt.", e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Ein Fehler ist aufgetreten. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    @Test
    public void testFetchNonExistingPayment() {
        try {
            getUnzerDE().fetchAuthorization("213");
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.310.100.003", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Die Zahlung 213 wurde nicht gefunden.", e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Die Zahlung wurde nicht gefunden. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    @Test
    public void testinvalidPutCustomer() throws HttpCommunicationException, ParseException {
        try {
            Customer customer = getUnzerDE().createCustomer(getMaximumCustomer(generateUuid()));
            Customer customerUpdate = new Customer(customer.getFirstname(), customer.getLastname());
            customerUpdate.setEmail("max");
            getUnzerDE().updateCustomer(customer.getId(), customerUpdate);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("Format der E-Mail ungültig.", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("Format der E-Mail ungültig.", getCustomerMessage("API.410.200.013", e.getPaymentErrorList()));
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
            getUnzerDE().createCustomer(customer);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.410.200.005", getCode("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("Länge des Vornamens unzulässig.", getMerchantMessage("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("Länge des Vornamens unzulässig.", getCustomerMessage("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("API.410.200.002", getCode("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("Länge des Nachnamens unzulässig.", getMerchantMessage("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("Länge des Nachnamens unzulässig.", getCustomerMessage("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("Format der E-Mail ungültig.", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("Format der E-Mail ungültig.", getCustomerMessage("API.410.200.013", e.getPaymentErrorList()));
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

    private String getCustomerMessage(String value, List<PaymentError> paymentErrorList) {
        for (PaymentError paymentError : paymentErrorList) {
            if (value.equals(paymentError.getCode())) return paymentError.getCustomerMessage();
        }
        return null;
    }

}
