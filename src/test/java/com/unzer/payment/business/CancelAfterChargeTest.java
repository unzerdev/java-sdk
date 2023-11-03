package com.unzer.payment.business;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.business.Keys.MARKETPLACE_KEY;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.BasePayment;
import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Basket;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.Payment;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.marketplace.MarketplaceCancel;
import com.unzer.payment.marketplace.MarketplaceCharge;
import com.unzer.payment.marketplace.MarketplacePayment;
import com.unzer.payment.paymenttypes.Card;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class CancelAfterChargeTest extends AbstractPaymentTest {

    @Test
    public void testFetchChargeWithId() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Charge charge = getUnzer().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertChargeEquals(initCharge, charge);
    }


    @Test
    public void testFullRefundWithId() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Cancel cancel = getUnzer().cancelCharge(initCharge.getPaymentId(), initCharge.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
    }

    @Test
    public void testFullRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Charge charge = getUnzer().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
        Cancel cancel = charge.cancel();
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
    }

    @Test
    public void testPartialRefundWithId() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Cancel cancel = getUnzer().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), new BigDecimal(0.1));
        assertNotNull(cancel);
    }

    @Test
    public void testPartialRefundWithCharge() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Charge charge = getUnzer().fetchCharge(initCharge.getPaymentId(), initCharge.getId());
        Cancel cancelExecuted = charge.cancel(new BigDecimal(0.1));
        assertNotNull(cancelExecuted);
        Cancel cancel = getUnzer().fetchCancel(initCharge.getPaymentId(), charge.getId(), cancelExecuted.getId());
        assertNotNull(cancel);
    }

    @Test
    public void testCancelAfterChargeChargeWithPaymentReference() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Cancel cancelReq = new Cancel();
        cancelReq.setPaymentReference("pmt-ref");
        Cancel cancelInit = initCharge.cancel(cancelReq);
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        assertEquals("pmt-ref", cancelInit.getPaymentReference());
        Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId()).getCancel(cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
        assertEquals("pmt-ref", cancel.getPaymentReference());
    }

    @Test
    public void testCancelAfterChargeChargeWithCancelObject() throws MalformedURLException, HttpCommunicationException {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        Cancel cancelReq = new Cancel();
        cancelReq.setPaymentReference("pmt-ref");
        cancelReq.setAmount(new BigDecimal(1.0));
        Cancel cancelInit = initCharge.cancel(cancelReq);
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        assertEquals("pmt-ref", cancelInit.getPaymentReference());
        assertEquals(new BigDecimal(1.0000).setScale(4), cancelInit.getAmount());
        Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId()).getCancel(cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
        assertEquals("pmt-ref", cancel.getPaymentReference());
        assertEquals(new BigDecimal(1.0000).setScale(4), cancel.getAmount());
    }

    @Disabled("Needs further configuration in Testdata")
    @Test
    public void testMarketplaceFullCancelChargeWithCard() throws MalformedURLException, HttpCommunicationException {
        String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
        String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;

        //create basket
        Basket maxBasket = getMaxTestBasketV1();
        maxBasket.setAmountTotalDiscount(null);

        maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
        maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);

        int basketItemCnt = maxBasket.getBasketItems().size();
        for (int i = 0; i < basketItemCnt; i++) {
            maxBasket.getBasketItems().get(i).setAmountDiscount(null);
        }

        Basket basket = getUnzer(MARKETPLACE_KEY).createBasket(maxBasket);

        //create card
        Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
        card = getUnzer(MARKETPLACE_KEY).createPaymentType(card);

        //marketplace charge
        MarketplaceCharge chargeRequest = getMarketplaceCharge(card.getId(), null, null, null, basket.getId(), null);
        chargeRequest.setAmount(maxBasket.getAmountTotalGross());

        MarketplaceCharge charge = getUnzer(MARKETPLACE_KEY).marketplaceCharge(chargeRequest);
        assertNotNull(charge.getId());
        assertNotNull(charge);
        assertEquals(BaseTransaction.Status.PENDING, charge.getStatus());
        assertEquals(participantId_2, charge.getProcessing().getParticipantId());

        //get marketplace payment
        MarketplacePayment payment = getUnzer(MARKETPLACE_KEY).fetchMarketplacePayment(charge.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getAuthorizationsList());
        assertEquals(1, payment.getChargesList().size());
        assertEquals(Payment.State.PENDING, payment.getPaymentState());

        //confirm authorization
        int redirectStatus = confirmMarketplacePendingTransaction(charge.getRedirectUrl().toString());
        await().atLeast(3, SECONDS).atMost(10, SECONDS);
        assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);

        //full cancel
        MarketplacePayment fullCancelPayment = charge.getPayment().marketplaceFullChargesCancel("test martketplace full cancel");
        assertNotNull(fullCancelPayment);
        assertEquals(Payment.State.CANCELED, fullCancelPayment.getPaymentState());
        assertEquals(2, fullCancelPayment.getChargesList().size());
        assertEquals(2, fullCancelPayment.getCancelList().size());
    }

    @Disabled("Needs further configuration in Testdata")
    @Test
    public void testMarketplacePartialCancelChargeWithCard() throws MalformedURLException, HttpCommunicationException {
        String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
        String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;

        //create basket
        Basket maxBasket = getMaxTestBasketV1();
        maxBasket.setAmountTotalDiscount(null);

        maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
        maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);

        int basketItemCnt = maxBasket.getBasketItems().size();
        for (int i = 0; i < basketItemCnt; i++) {
            maxBasket.getBasketItems().get(i).setAmountDiscount(null);
        }

        Basket basket = getUnzer(MARKETPLACE_KEY).createBasket(maxBasket);

        //create card
        Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
        card = getUnzer(MARKETPLACE_KEY).createPaymentType(card);

        //marketplace charge
        MarketplaceCharge chargeRequest = getMarketplaceCharge(card.getId(), null, null, null, basket.getId(), null);
        chargeRequest.setAmount(maxBasket.getAmountTotalGross());

        MarketplaceCharge charge = getUnzer(MARKETPLACE_KEY).marketplaceCharge(chargeRequest);
        assertNotNull(charge.getId());
        assertNotNull(charge);
        assertEquals(BaseTransaction.Status.PENDING, charge.getStatus());
        assertEquals(participantId_2, charge.getProcessing().getParticipantId());

        //confirm authorization
        int redirectStatus = confirmMarketplacePendingTransaction(charge.getRedirectUrl().toString());
        await().atLeast(3, SECONDS).atMost(10, SECONDS);
        assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);

        //get marketplace payment
        MarketplacePayment payment = getUnzer(MARKETPLACE_KEY).fetchMarketplacePayment(charge.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getAuthorizationsList());
        assertEquals(2, payment.getChargesList().size());
        assertEquals(Payment.State.COMPLETED, payment.getPaymentState());

        //partial cancel
        MarketplaceCancel cancelRequest = new MarketplaceCancel();
        cancelRequest.setPaymentReference("test marketplace partial cancel");
        cancelRequest.setCanceledBasket(this.buildCancelBasketByParticipant(maxBasket.getBasketItems(), charge.getProcessing().getParticipantId()));

        MarketplaceCancel cancelResponse = charge.cancel(cancelRequest);
        assertNotNull(cancelResponse);
        assertEquals(BaseTransaction.Status.SUCCESS, cancelResponse.getStatus());

        //assert payment
        MarketplacePayment paymentAfterCancel = cancelResponse.getPayment();
        assertEquals(BasePayment.State.COMPLETED, paymentAfterCancel.getPaymentState());
        assertEquals(1, paymentAfterCancel.getCancelList().size());
        assertEquals(payment.getAmountTotal(), paymentAfterCancel.getAmountTotal());
        assertEquals(payment.getAmountCharged().subtract(cancelResponse.getAmount()), paymentAfterCancel.getAmountCharged());
        assertEquals(cancelResponse.getAmount(), paymentAfterCancel.getAmountCanceled());
    }
}
