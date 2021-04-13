package com.unzer.payment.business.errors;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Card;
import org.junit.Test;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

public class ErrorTest extends AbstractPaymentTest {

    @Test
    public void testKeyMissing() throws MalformedURLException, HttpCommunicationException {
        try {
            getUnzer(null).authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.000.000.001", e.getPaymentErrorList().get(0).getCode());
            assertEquals("PrivateKey/PublicKey is missing", e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    // The given key something is unknown or invalid.
    // The key 's-priv-123' is invalid
    @Test
    public void testInvalidKey() throws MalformedURLException, HttpCommunicationException {
        try {
            getUnzer("s-priv-123").authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.000.002", e.getPaymentErrorList().get(0).getCode());
            assertEquals("The given key s-priv-123 is unknown or invalid.",
                    e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    // You do not have the permission to access this resource. This might happen you
    // are using either invalid credentials
    // Card resources can only be created directly with a valid PCI certification.
    // Please contact Unzer to grant permission for PCI level SAQ-D or SAQ-A EP
    @Test
    public void testPCILevelSaqA() throws HttpCommunicationException {
        try {
            getUnzer(publicKey1).createPaymentType(getPaymentTypeCard()); // Prod Sandbox
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.000.000.003", e.getPaymentErrorList().get(0).getCode());
            assertEquals(
                    "You do not have permission to access this resource. Please contact the owner of the shop.",
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
            getUnzer(privateKey2).fetchPaymentType(card.getId());  // Prod-Sandbox

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
    public void testMissingReturnUrl() throws MalformedURLException, HttpCommunicationException {
        try {
            Authorization authorization = getAuthorization(createPaymentTypeCard().getId());
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
    public void testPaymentTypeIdInvalid() throws MalformedURLException, HttpCommunicationException {
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
    public void testFetchNonExistingPayment() throws HttpCommunicationException {
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
    public void testFetchNonExistingCharge() throws MalformedURLException, HttpCommunicationException {
        Charge charge = getUnzer().charge(getCharge());
        Charge chargeFetched = getUnzer().fetchCharge(charge.getPaymentId(), "s-chg-200");
        assertNull(chargeFetched);
    }

    @Test
    public void testinvalidPutCustomer() throws HttpCommunicationException, ParseException {
        try {
            Customer customer = getUnzer().createCustomer(getMaximumCustomer(getRandomId()));
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
            assertEquals("API.410.200.015", getCode("API.410.200.015", e.getPaymentErrorList()));
            assertEquals("phone has invalid format.", getMerchantMessage("API.410.200.015", e.getPaymentErrorList()));
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
