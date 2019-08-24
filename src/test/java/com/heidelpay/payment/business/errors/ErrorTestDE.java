package com.heidelpay.payment.business.errors;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import com.heidelpay.payment.*;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

public class ErrorTestDE extends AbstractPaymentTest {

    // The given key something is unknown or invalid.
    // The key 's-priv-123' is invalid
    @Test
    public void testInvalidKey() throws MalformedURLException, HttpCommunicationException {
        try {
            getHeidelpayDE("s-priv-123").authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.000.002", e.getPaymentErrorList().get(0).getCode());
            assertEquals("The given key s-priv-123 is unknown or invalid.",
                    e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Bei der Überprüfung Ihrer Anfrage ist ein Problem aufgetreten. " +
                            "Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    // You do not have the permission to access this resource. This might happen you
    // are using either invalid credentials
    // Card resources can only be created directly with a valid PCI certification.
    // Please contact Heidelpay to grant permission for PCI level SAQ-D or SAQ-A EP
    @Test
    public void testPCILevelSaqA() throws HttpCommunicationException {
        try {
//			getHeidelpayDE("s-pub-2a10ehAb66CT6wXy43gJVqMvvOjGY5Gt").createPaymentType(getPaymentTypeCard()); // Development
            getHeidelpayDE("s-pub-2a10xITCUtmO2FlTP8RKB3OhdnKI4RmU").createPaymentType(getPaymentTypeCard()); // Prod Sandbox
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
    public void testInvalidAccess() throws HttpCommunicationException {
        Card card = createPaymentTypeCard();
        try {
//			getHeidelpayDE("s-priv-2a10SyGqMkJQoku5BdPSYUi3YO2iXQO9").fetchPaymentType(card.getId());  // Dev
            getHeidelpayDE("s-priv-2a1095rIVXy4IrNFXG6yQiguSAqNjciC").fetchPaymentType(card.getId());  // Prod-Sandbox

        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);

            assertEquals("API.710.100.001", e.getPaymentErrorList().get(0).getCode());
            assertEquals("The given payment method " + card.getId() + " is not found",
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
            Authorization authorization = getAuthorization(createPaymentTypeCard().getId());
            authorization.setReturnUrl(null);
            getHeidelpayDE().authorize(authorization);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.100.203", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Return URL is missing", e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Rückgabe-URL fehlt. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    @Test
    public void testPaymentTypeIdMissing() throws MalformedURLException, HttpCommunicationException {
        try {
            getHeidelpayDE().authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.200.143", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Resources type id is missing", e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Ein Fehler ist aufgetreten. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    @Test
    public void testFetchNonExistingPayment() throws HttpCommunicationException {
        try {
            getHeidelpayDE().fetchAuthorization("213");
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.310.100.003", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Payment not found with key 213", e.getPaymentErrorList().get(0).getMerchantMessage());
            assertEquals("Die Zahlung wurde nicht gefunden. Wenden Sie sich für weitere Informationen bitte an uns.",
                    e.getPaymentErrorList().get(0).getCustomerMessage());
        }
    }

    @Test
    public void testinvalidPutCustomer() throws HttpCommunicationException, ParseException {
        try {
            Customer customer = getHeidelpayDE().createCustomer(getMaximumCustomer(getRandomId()));
            Customer customerUpdate = new Customer(customer.getFirstname(), customer.getLastname());
            customerUpdate.setEmail("max");
            getHeidelpayDE().updateCustomer(customer.getId(), customerUpdate);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("email has invalid format.", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("Format der E-Mail ungültig", getCustomerMessage("API.410.200.013", e.getPaymentErrorList()));
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
            getHeidelpayDE().createCustomer(customer);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertEquals(4, e.getPaymentErrorList().size());
            assertEquals("API.410.200.005", getCode("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("firstName has invalid length.", getMerchantMessage("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("Länge des Vornamens unzulässig", getCustomerMessage("API.410.200.005", e.getPaymentErrorList()));
            assertEquals("API.410.200.002", getCode("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("lastName has invalid length.", getMerchantMessage("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("Länge des Nachnamens unzulässig", getCustomerMessage("API.410.200.002", e.getPaymentErrorList()));
            assertEquals("API.410.200.015", getCode("API.410.200.015", e.getPaymentErrorList()));
            assertEquals("phone has invalid format.", getMerchantMessage("API.410.200.015", e.getPaymentErrorList()));
            assertEquals("Format der Telefonnummer ungültig", getCustomerMessage("API.410.200.015", e.getPaymentErrorList()));
            assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("email has invalid format.", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
            assertEquals("Format der E-Mail ungültig", getCustomerMessage("API.410.200.013", e.getPaymentErrorList()));
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
