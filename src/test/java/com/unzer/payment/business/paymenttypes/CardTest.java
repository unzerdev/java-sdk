package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.enums.RecurrenceType;
import com.unzer.payment.paymenttypes.Card;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.Assert.*;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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

public class CardTest extends AbstractPaymentTest {

    @Test
    public void testCreateCardWithMerchantNotPCIDSSCompliant() throws HttpCommunicationException {
        ThrowingRunnable createCardWithMerchantNotPCIDSSCompliant = new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                Card card = new Card("4444333322221111", "03/20");
                card.setCvc("123");
                Unzer unzer = new Unzer(new HttpClientBasedRestCommunication(), privateKey3);
                unzer.createPaymentType(card);
            }

        };
        assertThrows(PaymentException.class, createCardWithMerchantNotPCIDSSCompliant);
    }

    @Test
    public void testCreateCardType() throws HttpCommunicationException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.setCardHolder("Beethoven");
        card.setBrand("VISA");
        card = getUnzer().createPaymentType(card);
        assertNotNull(card.getId());
        assertEquals("Beethoven", card.getCardHolder());
        assertEquals("VISA", card.getBrand());
        assertNotNull(card.getCardDetails());
        assertEquals("CREDIT", card.getCardDetails().getAccount());
        assertEquals("US", card.getCardDetails().getCountryIsoA2());
        assertEquals("UNITED STATES", card.getCardDetails().getCountryName());
        assertNull(card.getCardDetails().getCardType());
        assertNull(card.getCardDetails().getIssuerName());
        assertNull(card.getCardDetails().getIssuerUrl());
        assertNull(card.getCardDetails().getIssuerPhoneNumber());
        assertEquals("card", card.getMethod());
    }

    @Test
    public void testCreateCardTypeWith3DSFlag() throws HttpCommunicationException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.set3ds(false);
        card = getUnzer().createPaymentType(card);
        assertNotNull(card.getId());
        assertFalse(card.get3ds());
        card = (Card) getUnzer().fetchPaymentType(card.getId());
        assertNotNull(card.getId());
        assertFalse(card.get3ds());
    }

    @Test
    public void testAuthorizeAndPaymentCardType() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = card.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        Payment payment = authorization.getPayment();
        assertNotNull(authorization);
        assertNotNull(payment);
        assertNotNull(authorization.getId());
    }

    @Test
    public void testChargeCardType() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = card.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
    }

    @Test
    public void testChargeCardTypeWith3ds() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(getCharge(card.getId(), null, null, null, null, true, null));
        assertNotNull(charge);
    }

    @Test
    public void testFetchCardType() throws HttpCommunicationException {
        Card card = new Card("4444333322221111", "03/2099");
        card.setCvc("123");
        card.setCardHolder("Mozart");
        card.setBrand("VISA");

        Card createdCard = getUnzer().createPaymentType(card);
        assertNotNull(createdCard.getId());
        assertNotNull(createdCard.getId());
        assertEquals(maskString(card.getNumber(), 6, card.getNumber().length() - 4, '*'), createdCard.getNumber());
        assertEquals(card.getExpiryDate(), createdCard.getExpiryDate());
        assertNotNull(createdCard.getCvc());

        Card fetchedCard = (Card) getUnzer().fetchPaymentType(createdCard.getId());
        assertNotNull(fetchedCard.getId());
        assertEquals(maskString(card.getNumber(), 6, card.getNumber().length() - 4, '*'), fetchedCard.getNumber());
        assertEquals(card.getExpiryDate(), fetchedCard.getExpiryDate());
        assertNotNull(fetchedCard.getCvc());
        assertEquals("Mozart", fetchedCard.getCardHolder());
        assertEquals("VISA", fetchedCard.getBrand());
        assertNotNull(fetchedCard.getCardDetails());
        assertEquals("CREDIT", fetchedCard.getCardDetails().getAccount());
        assertEquals("US", fetchedCard.getCardDetails().getCountryIsoA2());
        assertEquals("UNITED STATES", fetchedCard.getCardDetails().getCountryName());
        assertNull(fetchedCard.getCardDetails().getCardType());
        assertNull(fetchedCard.getCardDetails().getIssuerName());
        assertNull(fetchedCard.getCardDetails().getIssuerUrl());
        assertNull(fetchedCard.getCardDetails().getIssuerPhoneNumber());
        assertNotNull(fetchedCard.getGeoLocation().getClientIp());
        assertNotNull(fetchedCard.getGeoLocation().getCountryIsoA2());
        assertEquals("card", fetchedCard.getMethod());
    }

    @Test
    public void testCardMailEmptyWhenNotSending() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        assertNull(card.getEmail());
    }

    @Test
    public void testCardMailNotEmptyWhenSending() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.setEmail(MAIL_STRING);
        card.setCardHolder(PERSON_STRING);
        card = getUnzer().createPaymentType(card);

        assertFalse(card.getEmail().isEmpty());
        assertEquals(MAIL_STRING, card.getEmail());
        assertNotNull(card.getEmail());
    }

    @Ignore("PAPI disallows removing email")
    @Test
    public void testCardMailEmptyWhenOverridingWithNull() throws HttpCommunicationException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.setEmail(MAIL_STRING);
        card = getUnzer().createPaymentType(card);

        card.setNumber("4444333322221111");
        card.setCvc("123");
        card.setExpiryDate("03/99");
        card.setEmail(null);
        card.setCardHolder(PERSON_STRING);
        card = getUnzer().updatePaymentType(card);

        assertNotNull(card.getEmail());
        assertTrue(card.getEmail().isEmpty());
    }

    @Test
    public void testCardMailOverridingWithValue() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.setEmail(null);
        card = getUnzer().createPaymentType(card);

        card.setNumber("4444333322221111");
        card.setCvc("123");
        card.setExpiryDate("03/99");
        card.setEmail(MAIL_STRING);
        card.setCardHolder(PERSON_STRING);
        card = getUnzer().updatePaymentType(card);

        assertFalse(card.getEmail().isEmpty());
        assertEquals(MAIL_STRING, card.getEmail());
        assertNotNull(card.getEmail());
    }

    @Test
    public void testCardMailInvalidValue() throws HttpCommunicationException {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.setEmail(INVALID_MAIL_STRING);
        try {
            card = getUnzer().createPaymentType(card);
        } catch (PaymentException e) {
            assertNotNull(e.getPaymentErrorList());
            assertTrue(e.getPaymentErrorList().size() > 0);
            assertEquals("API.710.100.216", e.getPaymentErrorList().get(0).getCode());
            assertEquals("Email has invalid format.", e.getPaymentErrorList().get(0).getMerchantMessage());
        }
    }

    @Test
    public void testCardRegisterRecurring() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        Recurring recurring = getUnzer().recurring(card.getId(), new URL("https://www.meinShop.de"));
        assertNotNull(recurring);
        assertNotNull(recurring.getRedirectUrl());
        assertEquals(AbstractTransaction.Status.PENDING, recurring.getStatus());
    }

    @Test
    public void testCardRegisterRecurringAdditionalPaymentInformationScheduled() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        Recurring recurring = getUnzer().recurring(card.getId(), null, null, new URL("https://www.meinShop.de"), RecurrenceType.SCHEDULED);
        assertEquals(AbstractTransaction.Status.PENDING, recurring.getStatus());
        assertNotNull(recurring.getAdditionalTransactionData());
        assertNotNull(recurring.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, recurring.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardRegisterRecurringAdditionalPaymentInformationUnscheduled() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        Recurring recurring = getUnzer().recurring(card.getId(), null, null, new URL("https://www.meinShop.de"), RecurrenceType.UNSCHEDULED);
        assertEquals(AbstractTransaction.Status.PENDING, recurring.getStatus());
        assertNotNull(recurring.getAdditionalTransactionData());
        assertNotNull(recurring.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, recurring.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceOneClick() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, true, RecurrenceType.ONECLICK);

        assertTrue(authorization.getCard3ds());
        assertEquals(AbstractTransaction.Status.PENDING, authorization.getStatus());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceOneClickWithout3DS() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, false, RecurrenceType.ONECLICK);

        assertFalse(authorization.getCard3ds());
        assertEquals(AbstractTransaction.Status.SUCCESS, authorization.getStatus());
        assertNull(authorization.getRedirectUrl());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceOneClick() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, true, RecurrenceType.ONECLICK);

        assertTrue(charge.getCard3ds());
        assertEquals(AbstractTransaction.Status.PENDING, charge.getStatus());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceOneClickWithout3DS() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, false, RecurrenceType.ONECLICK);

        assertFalse(charge.getCard3ds());
        assertEquals(AbstractTransaction.Status.SUCCESS, charge.getStatus());
        assertNull(charge.getRedirectUrl());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceScheduled() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, true, RecurrenceType.SCHEDULED);

        assertTrue(authorization.getCard3ds());
        assertEquals(AbstractTransaction.Status.PENDING, authorization.getStatus());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceScheduledWithout3DS() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, false, RecurrenceType.SCHEDULED);

        assertFalse(authorization.getCard3ds());
        assertEquals(AbstractTransaction.Status.SUCCESS, authorization.getStatus());
        assertNull(authorization.getRedirectUrl());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceScheduled() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, true, RecurrenceType.SCHEDULED);

        assertTrue(charge.getCard3ds());
        assertEquals(AbstractTransaction.Status.PENDING, charge.getStatus());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceScheduledWithout3DS() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, false, RecurrenceType.SCHEDULED);

        assertFalse(charge.getCard3ds());
        assertEquals(AbstractTransaction.Status.SUCCESS, charge.getStatus());
        assertNull(charge.getRedirectUrl());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceUnscheduled() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, true, RecurrenceType.UNSCHEDULED);

        assertTrue(authorization.getCard3ds());
        assertEquals(AbstractTransaction.Status.PENDING, authorization.getStatus());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceUnscheduledWithout3DS() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, false, RecurrenceType.UNSCHEDULED);

        assertFalse(authorization.getCard3ds());
        assertEquals(AbstractTransaction.Status.SUCCESS, authorization.getStatus());
        assertNull(authorization.getRedirectUrl());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceUnscheduled() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, true, RecurrenceType.UNSCHEDULED);

        assertTrue(charge.getCard3ds());
        assertEquals(AbstractTransaction.Status.PENDING, charge.getStatus());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceUnscheduledWithout3DS() throws HttpCommunicationException, MalformedURLException {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.meinShop.de"), null, false, RecurrenceType.UNSCHEDULED);

        assertFalse(charge.getCard3ds());
        assertEquals(AbstractTransaction.Status.SUCCESS, charge.getStatus());
        assertNull(charge.getRedirectUrl());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }
}
