package com.unzer.payment.integration.paymenttypes;

import static com.unzer.payment.business.Keys.ALT_LEGACY_PRIVATE_KEY;
import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Payment;
import com.unzer.payment.PaymentException;
import com.unzer.payment.Recurring;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.enums.RecurrenceType;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CardTransactionData;
import com.unzer.payment.paymenttypes.Card;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class CardTest extends AbstractPaymentTest {

    @Test
    public void testCreateCardWithMerchantNotPCIDSSCompliant() {
        assertThrows(PaymentException.class, () -> {
            Card card = new Card("4444333322221111", "03/20");
            card.setCvc("123");
            Unzer unzer = new Unzer(new HttpClientBasedRestCommunication(), ALT_LEGACY_PRIVATE_KEY);
            unzer.createPaymentType(card);
        });
    }

    @Test
    public void testCreateCardType() {
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
    public void testCreateCardTypeWith3DSFlag() {
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
    public void testAuthorizeAndPaymentCardType() {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = card.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.meinShop.de"));
        Payment payment = authorization.getPayment();
        assertNotNull(authorization);
        assertNotNull(payment);
        assertNotNull(authorization.getId());
    }

    @Test
    public void testChargeCardType() {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = card.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
    }

    @Test
    public void testChargeCardTypeWith3ds() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(getCharge(card.getId(), null, null, null, null, true, null));
        assertNotNull(charge);
    }

    @Test
    public void testFetchCardType() {
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
    public void testCardMailEmptyWhenNotSending() {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        assertNull(card.getEmail());
    }

    @Test
    public void testCardMailNotEmptyWhenSending() {
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.setEmail(MAIL_STRING);
        card.setCardHolder(PERSON_STRING);
        card = getUnzer().createPaymentType(card);

        assertFalse(card.getEmail().isEmpty());
        assertEquals(MAIL_STRING, card.getEmail());
        assertNotNull(card.getEmail());
    }

    @Disabled("PAPI disallows removing email")
    @Test
    public void testCardMailEmptyWhenOverridingWithNull() {
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
    public void testCardMailOverridingWithValue() {
        Unzer unzer = getUnzer();
        Card card = new Card("4444333322221111", "03/99");
        card.setCvc("123");
        card.setEmail(null);
        card = unzer.createPaymentType(card);

        card.setNumber("4444333322221111");
        card.setCvc("123");
        card.setExpiryDate("03/99");
        card.setEmail(MAIL_STRING);
        card.setCardHolder(PERSON_STRING);
        card = unzer.updatePaymentType(card);

        assertFalse(card.getEmail().isEmpty());
        assertEquals(MAIL_STRING, card.getEmail());
        assertNotNull(card.getEmail());
    }

    @Test
    public void testCardMailInvalidValue() {
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
    public void testCardRegisterRecurring() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        Recurring recurring = getUnzer().recurring(card.getId(), unsafeUrl("https://www.meinShop.de"));
        assertNotNull(recurring);
        assertNotNull(recurring.getRedirectUrl());
        assertEquals(BaseTransaction.Status.PENDING, recurring.getStatus());
    }

    @Test
    public void testCardRegisterRecurringAdditionalPaymentInformationScheduled() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        Recurring recurring = getUnzer().recurring(card.getId(), null, null, unsafeUrl("https://www.meinShop.de"), RecurrenceType.SCHEDULED);
        assertEquals(BaseTransaction.Status.PENDING, recurring.getStatus());
        assertNotNull(recurring.getAdditionalTransactionData());
        assertNotNull(recurring.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, recurring.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardRegisterRecurringAdditionalPaymentInformationUnscheduled() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);

        Recurring recurring = getUnzer().recurring(card.getId(), null, null, unsafeUrl("https://www.meinShop.de"), RecurrenceType.UNSCHEDULED);
        assertEquals(BaseTransaction.Status.PENDING, recurring.getStatus());
        assertNotNull(recurring.getAdditionalTransactionData());
        assertNotNull(recurring.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, recurring.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceOneClick() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, true, RecurrenceType.ONECLICK);

        assertTrue(authorization.getCard3ds());
        assertEquals(BaseTransaction.Status.PENDING, authorization.getStatus());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceOneClickWithout3DS() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, false, RecurrenceType.ONECLICK);

        assertFalse(authorization.getCard3ds());
        assertEquals(BaseTransaction.Status.SUCCESS, authorization.getStatus());
        assertNull(authorization.getRedirectUrl());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceOneClick() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, true, RecurrenceType.ONECLICK);

        assertTrue(charge.getCard3ds());
        assertEquals(BaseTransaction.Status.PENDING, charge.getStatus());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceOneClickWithout3DS() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, false, RecurrenceType.ONECLICK);

        assertFalse(charge.getCard3ds());
        assertEquals(BaseTransaction.Status.SUCCESS, charge.getStatus());
        assertNull(charge.getRedirectUrl());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.ONECLICK, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceScheduled() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, true, RecurrenceType.SCHEDULED);

        assertTrue(authorization.getCard3ds());
        assertEquals(BaseTransaction.Status.PENDING, authorization.getStatus());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceScheduledWithout3DS() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, false, RecurrenceType.SCHEDULED);

        assertFalse(authorization.getCard3ds());
        assertEquals(BaseTransaction.Status.SUCCESS, authorization.getStatus());
        assertNull(authorization.getRedirectUrl());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceScheduled() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, true, RecurrenceType.SCHEDULED);

        assertTrue(charge.getCard3ds());
        assertEquals(BaseTransaction.Status.PENDING, charge.getStatus());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceScheduledWithout3DS() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, false, RecurrenceType.SCHEDULED);

        assertFalse(charge.getCard3ds());
        assertEquals(BaseTransaction.Status.SUCCESS, charge.getStatus());
        assertNull(charge.getRedirectUrl());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.SCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceUnscheduled() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, true, RecurrenceType.UNSCHEDULED);

        assertTrue(authorization.getCard3ds());
        assertEquals(BaseTransaction.Status.PENDING, authorization.getStatus());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardAuthorizeAdditionalTransactionDataRecurrenceUnscheduledWithout3DS() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, false, RecurrenceType.UNSCHEDULED);

        assertFalse(authorization.getCard3ds());
        assertEquals(BaseTransaction.Status.SUCCESS, authorization.getStatus());
        assertNull(authorization.getRedirectUrl());
        assertNotNull(authorization.getAdditionalTransactionData());
        assertNotNull(authorization.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, authorization.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceUnscheduled() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, true, RecurrenceType.UNSCHEDULED);

        assertTrue(charge.getCard3ds());
        assertEquals(BaseTransaction.Status.PENDING, charge.getStatus());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    public void testCardChargeAdditionalTransactionDataRecurrenceUnscheduledWithout3DS() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, "03/99");
        card.setCvc("123");
        card = getUnzer().createPaymentType(card);
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.meinShop.de"), null, false, RecurrenceType.UNSCHEDULED);

        assertFalse(charge.getCard3ds());
        assertEquals(BaseTransaction.Status.SUCCESS, charge.getStatus());
        assertNull(charge.getRedirectUrl());
        assertNotNull(charge.getAdditionalTransactionData());
        assertNotNull(charge.getAdditionalTransactionData().getCard());
        assertEquals(RecurrenceType.UNSCHEDULED, charge.getAdditionalTransactionData().getCard().getRecurrenceType());
    }

    @Test
    @Disabled("Not implemented on SBX")
    public void testAdditionalTransactionData_Liability() {
        Unzer unzer = getUnzer();
        final CardTransactionData.Liability liability = CardTransactionData.Liability.MERCHANT;

        Card card = unzer.createPaymentType(
                new Card(NO_3DS_VISA_CARD_NUMBER, "01/30")
                        .setCvc("123")
                        .set3ds(false)
        );

        Authorization authorization = (Authorization) new Authorization()
                .setTypeId(card.getId())
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y")
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setCard(
                                        new CardTransactionData()
                                                .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                )
                );
        authorization = unzer.authorize(authorization);

        Authorization fetchAuthorization = unzer.fetchAuthorization(authorization.getPaymentId());
        assertEquals(liability, fetchAuthorization.getAdditionalTransactionData().getCard().getLiability());
    }

    @Test
    public void testCardLowValueExemptionsForAuth() {
        Unzer unzer = getUnzer();

        Card card = unzer.createPaymentType(
                new Card(NO_3DS_VISA_CARD_NUMBER, "01/30")
                        .setCvc("123")
                        .set3ds(false)
        );

        Authorization authorization = (Authorization) new Authorization()
                .setTypeId(card.getId())
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y")
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setCard(
                                        new CardTransactionData()
                                                .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                                .setExemptionType(CardTransactionData.ExemptionType.LVP)
                                )
                );
        authorization = unzer.authorize(authorization);

        Authorization fetchAuthorization = unzer.fetchAuthorization(authorization.getPaymentId());
        assertEquals(CardTransactionData.ExemptionType.LVP, fetchAuthorization.getAdditionalTransactionData().getCard().getExemptionType());
    }

    @Test
    public void testCardLowValueExemptionsForCharge() {
        Unzer unzer = getUnzer();

        Card card = unzer.createPaymentType(
                new Card(NO_3DS_VISA_CARD_NUMBER, "01/30")
                        .setCvc("123")
                        .set3ds(false)
        );

        Charge charge = (Charge) new Charge()
                .setTypeId(card.getId())
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.getInstance("EUR"))
                .setOrderId("ord-Hi686u4Q4Y")
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setCard(
                                        new CardTransactionData()
                                                .setRecurrenceType(RecurrenceType.UNSCHEDULED)
                                                .setExemptionType(CardTransactionData.ExemptionType.LVP)
                                )
                );
        unzer.charge(charge);

        Charge fetchCharge = unzer.fetchCharge(charge.getPaymentId(), "s-chg-1");
        assertEquals(CardTransactionData.ExemptionType.LVP, fetchCharge.getAdditionalTransactionData().getCard().getExemptionType());
    }

}
