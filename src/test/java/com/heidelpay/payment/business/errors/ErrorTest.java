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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class ErrorTest extends AbstractPaymentTest {

    @Test
    public void testKeyMissing() throws MalformedURLException, HttpCommunicationException {
        try {
            getHeidelpay(null).authorize(getAuthorization(""));
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
            getHeidelpay("s-priv-123").authorize(getAuthorization(""));
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
    // Please contact Heidelpay to grant permission for PCI level SAQ-D or SAQ-A EP
    @Test
    public void testPCILevelSaqA() throws HttpCommunicationException {
        try {
//			getHeidelpay("s-pub-2a10ehAb66CT6wXy43gJVqMvvOjGY5Gt").createPaymentType(getPaymentTypeCard()); // Development
            getHeidelpay("s-pub-2a10xITCUtmO2FlTP8RKB3OhdnKI4RmU").createPaymentType(getPaymentTypeCard()); // Prod Sandbox
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
    @Test
    public void testInvalidAccess() throws HttpCommunicationException {
        Card card = createPaymentTypeCard();
        try {
//			getHeidelpay("s-priv-2a10SyGqMkJQoku5BdPSYUi3YO2iXQO9").fetchPaymentType(card.getId());  // Dev
            getHeidelpay("s-priv-2a1095rIVXy4IrNFXG6yQiguSAqNjciC").fetchPaymentType(card.getId());  // Prod-Sandbox

        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.710.100.001", e.getPaymentErrorList().get(0).getCode());
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
            getHeidelpay().authorize(authorization);
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
            getHeidelpay().authorize(getAuthorization(""));
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.320.200.163", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Resources type id is invalid.", e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    @Test
    public void testFetchNonExistingPayment() throws HttpCommunicationException {
        try {
            getHeidelpay().fetchAuthorization("213");
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.310.100.003", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Payment not found with key 213", e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    @Test
    public void testFetchNonExistingCharge() throws MalformedURLException, HttpCommunicationException {
        Charge charge = getHeidelpay().charge(getCharge());
        Charge chargeFetched = getHeidelpay().fetchCharge(charge.getPaymentId(), "s-chg-200");
        assertNull(chargeFetched);
    }

    @Test
    public void testinvalidPutCustomer() throws HttpCommunicationException, ParseException {
        try {
            Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
            Customer customerUpdate = new Customer(customer.getFirstname(), customer.getLastname());
            customerUpdate.setEmail("max");
            getHeidelpay().updateCustomer(customer.getId(), customerUpdate);
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
            getHeidelpay().createCustomer(customer);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertEquals(4, e.getPaymentErrorList().size());
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
