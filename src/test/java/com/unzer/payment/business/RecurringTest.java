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


import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.Metadata;
import com.unzer.payment.Recurring;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Card;
import com.unzer.payment.paymenttypes.Paypal;
import com.unzer.payment.paymenttypes.SepaDirectDebit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;

public class RecurringTest extends AbstractPaymentTest {
    @Test
    public void testRecurringCardWithoutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
        String typeId = createPaymentTypeCard(getUnzer(), "4711100000000000").getId();
        Recurring recurring = getUnzer().recurring(typeId, new URL("https://www.unzer.com"));
        assertRecurring(recurring, Recurring.Status.PENDING);

        Card type = (Card) getUnzer().fetchPaymentType(typeId);
        assertEquals(false, type.getRecurring());

    }

    @Test
    public void testRecurringCardWitCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
        String typeId = createPaymentTypeCard(getUnzer(), "4711100000000000").getId();
        Recurring recurring = getUnzer().recurring(typeId, customer.getId(), new URL("https://www.unzer.com"));
        assertRecurring(recurring, Recurring.Status.PENDING);
        assertNotNull(recurring.getRedirectUrl());

        Card type = (Card) getUnzer().fetchPaymentType(typeId);
        assertEquals(false, type.getRecurring());
    }

    @Test
    public void testRecurringCardWitCustomerWithCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
        Recurring recurring = getUnzer().recurring(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), customer.getCustomerId(), new URL("https://www.unzer.com"));
        assertRecurring(recurring, Recurring.Status.PENDING);
        assertNotNull(recurring.getRedirectUrl());
    }

    @Test
    public void testRecurringCardWitCustomerAndMetadata() throws MalformedURLException, HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
        Metadata metadata = getUnzer().createMetadata(getTestMetadata());
        Recurring recurring = getUnzer().recurring(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), customer.getId(), metadata.getId(), new URL("https://www.unzer.com"));
        assertRecurring(recurring, Recurring.Status.PENDING);
        assertNotNull(recurring.getRedirectUrl());
    }

    @Test
    public void testRecurringPaypalWithoutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
        Paypal paypal = new Paypal();
        paypal = getUnzer().createPaymentType(paypal);
        Recurring recurring = getUnzer().recurring(paypal.getId(), new URL("https://www.unzer.com"));
        assertRecurring(recurring, Recurring.Status.PENDING);

        Paypal type = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
        assertEquals(false, type.getRecurring());
    }

    @Test
    public void testRecurringPaypalWitCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
        Paypal paypal = new Paypal();
        paypal = getUnzer().createPaymentType(paypal);
        Recurring recurring = getUnzer().recurring(paypal.getId(), customer.getId(), new URL("https://www.unzer.com"));
        assertRecurring(recurring, Recurring.Status.PENDING);
        assertNotNull(recurring.getRedirectUrl());

        Paypal type = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
        assertEquals(false, type.getRecurring());
    }

    @Test
    public void testRecurringCardDuringCharge() throws MalformedURLException, HttpCommunicationException, ParseException {
        String typeId = createPaymentTypeCard(getUnzer(), "4711100000000000").getId();
        Card type = (Card) getUnzer().fetchPaymentType(typeId);
        assertEquals(false, type.getRecurring());

        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), typeId, new URL("https://www.unzer.com"), false);
        assertNull(charge.getRedirectUrl());

        type = (Card) getUnzer().fetchPaymentType(typeId);
        assertEquals(true, type.getRecurring());
    }

    @Test
    public void testRecurringSepaDirectDebitDuringCharge() throws MalformedURLException, HttpCommunicationException, ParseException {
        SepaDirectDebit sdd = createPaymentTypeSepaDirectDebit();
        sdd = (SepaDirectDebit) getUnzer().fetchPaymentType(sdd.getId());
        assertEquals(false, sdd.getRecurring());

        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), sdd.getId(), new URL("https://www.unzer.com"));
        assertNull(charge.getRedirectUrl());

        sdd = (SepaDirectDebit) getUnzer().fetchPaymentType(sdd.getId());
        assertEquals(true, sdd.getRecurring());
    }


    private void assertRecurring(Recurring recurring, Recurring.Status status) {
        assertNotNull(recurring);
        assertNotNull(recurring.getProcessing());
        assertNotNull(recurring.getProcessing().getUniqueId());

        assertNotNull(recurring.getUnzer());
        // Bug with Paypal that no date is returned in recurring call: https://heidelpay.atlassian.net/browse/AHC-1724
//		assertNotNull(recurring.getDate());
        assertNotNull(recurring.getRedirectUrl());
        assertEquals(status, recurring.getStatus());
    }

}
