package com.unzer.payment.business;

import com.unzer.payment.*;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.marketplace.MarketplaceAuthorization;
import com.unzer.payment.marketplace.MarketplaceCharge;
import com.unzer.payment.marketplace.MarketplacePayment;
import com.unzer.payment.paymenttypes.Card;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.business.Keys.MARKETPLACE_KEY;
import static com.unzer.payment.util.Uuid.generateUuid;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ChargeAfterAuthorizationTest extends AbstractPaymentTest {

    @Test
    public void fetchAuthorization() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId()));
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        assertNotNull(authorization);
    }

    @Test
    public void fullChargeAfterAuthorization() {
        String orderId = generateUuid();
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, orderId, null, null, false));
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        Charge charge = authorization.charge();
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals(orderId, authorize.getOrderId());
        assertEquals(orderId, authorization.getOrderId());
        assertEquals(orderId, charge.getOrderId());
    }

    @Test
    public void fullChargeAfterAuthorizationUnzer() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Charge charge = getUnzer().chargeAuthorization(authorize.getPaymentId());
        assertNotNull(charge);
        assertNotNull(charge.getId());
    }

    @Test
    public void partialChargeAfterAuthorization() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        Charge charge = authorization.charge(new BigDecimal("0.1"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
    }

    @Test
    public void chargeAfterAuthorizationWithPaymentReference() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Charge charge = authorize.charge(new BigDecimal("1.0"), "pmt-ref");
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals("pmt-ref", charge.getPaymentReference());
    }

    @Disabled("Needs further configuration in Testdata")
    @Test
    public void testMarketplaceFullAuthorizeCharge() throws MalformedURLException, HttpCommunicationException {
        String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
        String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;

        // create basket
        Basket maxBasket = getMaxTestBasketV1();
        maxBasket.setAmountTotalDiscount(null);

        maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
        maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);

        int basketItemCnt = maxBasket.getBasketItems().size();
        for (int i = 0; i < basketItemCnt; i++) {
            maxBasket.getBasketItems().get(i).setAmountDiscount(null);
        }

        Basket basket = getUnzer(MARKETPLACE_KEY).createBasket(maxBasket);

        // create card
        Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
        card = getUnzer(MARKETPLACE_KEY).createPaymentType(card);

        // marketplace authorization
        MarketplaceAuthorization authorizeRequest = getMarketplaceAuthorization(card.getId(), null, null, null,
                basket.getId(), null);
        authorizeRequest.setAmount(maxBasket.getAmountTotalGross());

        MarketplaceAuthorization authorize = getUnzer(MARKETPLACE_KEY).marketplaceAuthorize(authorizeRequest);
        assertNotNull(authorize.getId());
        assertNotNull(authorize);
        assertEquals(BaseTransaction.Status.PENDING, authorize.getStatus());
        assertEquals(participantId_2, authorize.getProcessing().getParticipantId());

        //confirm authorization
        int redirectStatus = confirmMarketplacePendingTransaction(authorize.getRedirectUrl().toString());
        await().atLeast(5, SECONDS).atMost(10, SECONDS);
        assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);

        //get payment
        MarketplacePayment payment = getUnzer(MARKETPLACE_KEY).fetchMarketplacePayment(authorize.getPaymentId());
        assertEquals(2, payment.getAuthorizationsList().size());
        assertEquals(Payment.State.PENDING, payment.getPaymentState());

        //full charge authorizations
        MarketplacePayment fullCapturePayment = payment.fullChargeAuthorizations("test full marketplace full capture.");
        assertEquals(payment.getId(), fullCapturePayment.getId());
        assertEquals(2, fullCapturePayment.getAuthorizationsList().size());
        assertEquals(2, fullCapturePayment.getChargesList().size());
        assertEquals(Payment.State.COMPLETED, fullCapturePayment.getPaymentState());
    }

    @Disabled("Needs further configuration in Testdata")
    @Test
    public void testMarketplaceAuthorizeCharge() throws MalformedURLException, HttpCommunicationException {
        String participantId_1 = MARKETPLACE_PARTICIPANT_ID_1;
        String participantId_2 = MARKETPLACE_PARTICIPANT_ID_2;

        // create basket
        Basket maxBasket = getMaxTestBasketV1();
        maxBasket.setAmountTotalDiscount(null);

        maxBasket.getBasketItems().get(0).setParticipantId(participantId_1);
        maxBasket.getBasketItems().get(1).setParticipantId(participantId_2);

        int basketItemCnt = maxBasket.getBasketItems().size();
        for (int i = 0; i < basketItemCnt; i++) {
            maxBasket.getBasketItems().get(i).setAmountDiscount(null);
        }

        Basket basket = getUnzer(MARKETPLACE_KEY).createBasket(maxBasket);

        // create card
        Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
        card = getUnzer(MARKETPLACE_KEY).createPaymentType(card);

        // marketplace authorization
        MarketplaceAuthorization authorizeRequest = getMarketplaceAuthorization(card.getId(), null, null, null,
                basket.getId(), null);
        authorizeRequest.setAmount(maxBasket.getAmountTotalGross());

        MarketplaceAuthorization authorize = getUnzer(MARKETPLACE_KEY).marketplaceAuthorize(authorizeRequest);
        assertNotNull(authorize.getId());
        assertNotNull(authorize);
        assertEquals(BaseTransaction.Status.PENDING, authorize.getStatus());
        assertEquals(participantId_2, authorize.getProcessing().getParticipantId());

        //confirm authorization
        int redirectStatus = confirmMarketplacePendingTransaction(authorize.getRedirectUrl().toString());
        await().atLeast(5, SECONDS).atMost(10, SECONDS);
        assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);

        //charge authorization
        MarketplaceCharge chargeAuthorization = new MarketplaceCharge();
        chargeAuthorization.setAmount(BigDecimal.TEN);
        chargeAuthorization.setPaymentReference("test single marketplace authorize charge");
        chargeAuthorization = authorize.charge(chargeAuthorization);

        //get payment
        MarketplacePayment payment = getUnzer(MARKETPLACE_KEY).fetchMarketplacePayment(authorize.getPaymentId());
        assertEquals(2, payment.getAuthorizationsList().size());
        assertEquals(Payment.State.PARTLY, payment.getPaymentState());
        assertEquals(2, payment.getAuthorizationsList().size());
        assertEquals(1, payment.getChargesList().size());
        assertEquals(chargeAuthorization.getProcessing().getUniqueId(), payment.getChargesList().get(0).getProcessing().getUniqueId());
    }
}
