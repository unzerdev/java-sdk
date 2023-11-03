package com.unzer.payment.business;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.business.Keys.MARKETPLACE_KEY;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.BasePayment;
import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Authorization;
import com.unzer.payment.Basket;
import com.unzer.payment.Cancel;
import com.unzer.payment.Payment;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.marketplace.MarketplaceAuthorization;
import com.unzer.payment.marketplace.MarketplaceCancel;
import com.unzer.payment.marketplace.MarketplacePayment;
import com.unzer.payment.paymenttypes.Card;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class CancelAfterAuthorizationTest extends AbstractPaymentTest {

    @Test
    public void fetchAuthorization() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId()));
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        assertNotNull(authorization);
    }

    @Test
    public void fullCancelAfterAuthorization() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        Cancel cancel = authorization.cancel();
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
    }

    @Test
    public void partialCancelPayment() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Payment payment = getUnzer().fetchPayment(authorize.getPaymentId());
        Cancel cancel = payment.cancel(new BigDecimal(0.1));
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(getBigDecimal("0.1000"), cancel.getAmount());
    }

    @Test
    public void partialCancelAfterAuthorization() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        Cancel cancel = authorization.cancel(new BigDecimal(0.1));
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(getBigDecimal("0.1000"), cancel.getAmount());
    }

    @Test
    public void partialCancelAfterAuthorizationUnzer() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Cancel cancel = getUnzer().cancelAuthorization(authorize.getPaymentId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(getBigDecimal("10.0000"), cancel.getAmount());
    }

    @Test
    public void fetchCancelUnzer() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Cancel cancelExecuted = getUnzer().cancelAuthorization(authorize.getPaymentId());
        assertNotNull(cancelExecuted);
        assertNotNull(cancelExecuted.getId());
        assertEquals(getBigDecimal("10.0000"), cancelExecuted.getAmount());
        Cancel cancel = getUnzer().fetchCancel(authorize.getPaymentId(), cancelExecuted.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
    }

    @Test
    public void partialCancelAfterAuthorizationIsListFilled() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        Cancel cancel = authorization.cancel(BigDecimal.ONE);
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        Cancel cancel2 = authorization.cancel(BigDecimal.ONE);
        assertNotNull(cancel2);
        assertNotNull(cancel2.getId());
        authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        assertNotNull(authorization.getCancelList());
        assertEquals(2, authorization.getCancelList().size());
        assertEquals(2, authorization.getPayment().getAuthorization().getCancelList().size());
    }

    @Test
    public void cancelWithPaymentReference() {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Cancel cancelReq = new Cancel();
        cancelReq.setPaymentReference("pmt-ref");
        Cancel cancelExecuted = authorize.cancel(cancelReq);
        assertNotNull(cancelExecuted);
        assertNotNull(cancelExecuted.getId());
        assertEquals(getBigDecimal("10.0000"), cancelExecuted.getAmount());
        assertEquals("pmt-ref", cancelExecuted.getPaymentReference());
        Cancel cancel = getUnzer().fetchCancel(authorize.getPaymentId(), cancelExecuted.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals("pmt-ref", cancel.getPaymentReference());
    }

    @Disabled("Needs further configuration in Testdata")
    @Test
    public void testMarketplaceFullAuthorizeCancel() throws MalformedURLException, HttpCommunicationException {
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

        //full cancel
        MarketplacePayment fullCancelPayment = authorize.getPayment().marketplaceFullAuthorizeCancel("test martketplace full cancel");
        assertNotNull(fullCancelPayment);
        assertEquals(Payment.State.CANCELED, fullCancelPayment.getPaymentState());
        assertEquals(2, fullCancelPayment.getAuthorizationsList().size());
        assertEquals(2, fullCancelPayment.getCancelList().size());
    }

    @Disabled("Needs further configuration in Testdata")
    @Test
    public void testMarketplacePartialAuthorizeCancel() throws MalformedURLException, HttpCommunicationException {
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

        //fetch payment
        MarketplacePayment payment = getUnzer(MARKETPLACE_KEY).fetchMarketplacePayment(authorize.getPaymentId());

        //partial cancel
        MarketplaceCancel cancelRequest = new MarketplaceCancel();
        cancelRequest.setPaymentReference("test marketplace partial cancel");
        cancelRequest.setCanceledBasket(this.buildCancelBasketByParticipant(maxBasket.getBasketItems(), authorize.getProcessing().getParticipantId()));

        MarketplaceCancel cancelResponse = authorize.cancel(cancelRequest);
        assertNotNull(cancelResponse);
        assertEquals(BaseTransaction.Status.SUCCESS, cancelResponse.getStatus());
        assertEquals(authorize.getProcessing().getParticipantId(), cancelResponse.getProcessing().getParticipantId());

        //assert payment
        MarketplacePayment paymentAfterCancel = cancelResponse.getPayment();
        assertEquals(BasePayment.State.PENDING, payment.getPaymentState());
        assertEquals(payment.getAmountTotal().subtract(cancelResponse.getAmount()), paymentAfterCancel.getAmountTotal());
    }
}
