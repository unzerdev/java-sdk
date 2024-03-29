package com.unzer.payment.business;

import com.unzer.payment.*;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.marketplace.MarketplaceAuthorization;
import com.unzer.payment.marketplace.MarketplacePayment;
import com.unzer.payment.paymenttypes.Card;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Currency;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.util.Uuid.generateUuid;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AuthorizationTest extends AbstractPaymentTest {

    @Test
    public void testAuthorizeWithAuthorizationObject() throws MalformedURLException, HttpCommunicationException {
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        assertNotNull(authorize.getId());
        assertNotNull(authorize);
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testAuthorizeWithTypeId() throws MalformedURLException, HttpCommunicationException {
        Authorization authorize = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testAuthorizeSuccess() throws MalformedURLException, HttpCommunicationException {
        Authorization authorize = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
        assertEquals(BaseTransaction.Status.SUCCESS, authorize.getStatus());
    }

    @Test
    public void testAuthorizeWithPaymentType() throws MalformedURLException, HttpCommunicationException {
        Unzer unzer = getUnzer(Keys.DEFAULT);
        LocalDate locaDateNow = LocalDate.now();
        Card card = new Card("4444333322221111", "12/" + (locaDateNow.getYear() + 1));
        Authorization authorize = unzer.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.unzer.com"), false);
        assertNotNull(authorize);
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
        assertNotNull(authorize.getId());
    }

    @Test
    public void testAuthorizeReturnPaymentTypeAndCustomer() throws MalformedURLException, HttpCommunicationException {
        Unzer unzer = getUnzer(Keys.DEFAULT);
        LocalDate locaDateNow = LocalDate.now();
        Card card = new Card("4444333322221111", "12/" + (locaDateNow.getYear() + 1));
        Customer customer = new Customer("Max", "Mustermann");
        Authorization authorize = unzer.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.unzer.com"), customer, false);
        Payment payment = authorize.getPayment();
        assertNotNull(payment.getPaymentType());
        assertNotNull(payment.getCustomer());
        assertNotNull(authorize);
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testAuthorizeWithCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
        Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));
        Authorization authorize = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), customer.getCustomerId(), false);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testAuthorizeWithReturnUrl() throws MalformedURLException, HttpCommunicationException {
        Authorization authorize = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testAuthorizeWithCustomerTypeReturnUrl() throws MalformedURLException, HttpCommunicationException {
        Unzer unzer = getUnzer(Keys.DEFAULT);
        LocalDate locaDateNow = LocalDate.now();
        Card card = new Card("4444333322221111", "12/" + (locaDateNow.getYear() + 1));
        Customer customer = new Customer("Max", "Mustermann");
        Authorization authorize = unzer.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.unzer.com"), customer, false);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testAuthorizeWithCustomerIdReturnUrl() throws MalformedURLException, HttpCommunicationException, ParseException {
        Customer maxCustomer = getMaximumCustomer(generateUuid());
        Authorization authorize = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), maxCustomer.getId(), false);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testFetchAuthorization() throws MalformedURLException, HttpCommunicationException {
        Authorization authorize = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), new URL("https://www.unzer.com"), false);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        Authorization authorization = getUnzer().fetchAuthorization(authorize.getPaymentId());
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
    }

    @Test
    public void testAuthorizeWithOrderId() throws MalformedURLException, HttpCommunicationException {
        String orderId = generateUuid();
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, orderId, null, null, false));
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertNotNull(authorize.getMessage().getCustomer());
        Authorization authorization = getUnzer().fetchAuthorization(orderId);
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(orderId, authorization.getOrderId());

    }

    @Test
    public void testAuthorizeCard3dsFalse() throws MalformedURLException, HttpCommunicationException {
        String orderId = generateUuid();
        Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, orderId, null, null, false));
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.100.112", authorize.getMessage().getCode());
        assertEquals(Authorization.Status.SUCCESS, authorize.getStatus());
//		assertTrue(authorize.getCard3ds());
        Authorization authorization = getUnzer().fetchAuthorization(orderId);
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(orderId, authorization.getOrderId());
//		assertTrue(authorization.getCard3ds());
    }

    @Test
    public void testAuthorizeCard3dsTrue() throws MalformedURLException, HttpCommunicationException {
        String orderId = generateUuid();
        Unzer unzer = getUnzer();
        Authorization authorize = unzer.authorize(getAuthorization(createPaymentTypeCard(unzer, "4711100000000000").getId(), null, orderId, null, null, true));
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("COR.000.200.000", authorize.getMessage().getCode());
        assertEquals(Authorization.Status.PENDING, authorize.getStatus());
//		assertTrue(authorize.getCard3ds());
        Authorization authorization = unzer.fetchAuthorization(orderId);
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(orderId, authorization.getOrderId());
//		assertTrue(authorization.getCard3ds());
    }

    @Test
    public void testAuthorizeWithPaymentReference() throws MalformedURLException, HttpCommunicationException {
        Authorization authorization = getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId());
        authorization.setPaymentReference("pmt-ref");
        Authorization authorize = getUnzer().authorize(authorization);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("pmt-ref", authorize.getPaymentReference());
    }

    @Test
    public void testAuthorizeWithAuthorizeObject() throws MalformedURLException, HttpCommunicationException {
        Authorization authorization = getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId());
        authorization.setPaymentReference("pmt-ref");
        authorization.setAmount(new BigDecimal("1.0"));
        Authorization authorize = getUnzer().authorize(authorization);
        assertNotNull(authorize);
        assertNotNull(authorize.getId());
        assertEquals("pmt-ref", authorize.getPaymentReference());
        assertEquals(new BigDecimal("1.0000").setScale(4), authorize.getAmount());
    }

    @Disabled("Needs further configuration in Testdata")
    @Test
    public void testMarketplaceAuthorize() throws MalformedURLException, HttpCommunicationException {
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

        Basket basket = getUnzer(Keys.MARKETPLACE_KEY).createBasket(maxBasket);

        //create card
        Card card = getPaymentTypeCard(NO_3DS_VISA_CARD_NUMBER); //do not change card number except error case
        card = getUnzer(Keys.MARKETPLACE_KEY).createPaymentType(card);

        //marketplace authorization
        MarketplaceAuthorization authorizeRequest = getMarketplaceAuthorization(card.getId(), null, null, null, basket.getId(), null);
        authorizeRequest.setAmount(maxBasket.getAmountTotalGross());

        MarketplaceAuthorization authorize = getUnzer(Keys.MARKETPLACE_KEY).marketplaceAuthorize(authorizeRequest);
        assertNotNull(authorize.getId());
        assertNotNull(authorize);
        assertEquals(BaseTransaction.Status.PENDING, authorize.getStatus());
        assertEquals(participantId_2, authorize.getProcessing().getParticipantId());

        int redirectStatus = confirmMarketplacePendingTransaction(authorize.getRedirectUrl().toString());
        await().atLeast(5, SECONDS).atMost(10, SECONDS);
        assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, redirectStatus);

        //get marketplace payment
        MarketplacePayment payment = getUnzer(Keys.MARKETPLACE_KEY).fetchMarketplacePayment(authorize.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getAuthorizationsList());
        assertEquals(2, payment.getAuthorizationsList().size());
        assertEquals(Payment.State.PENDING, payment.getPaymentState());

        //get marketplace authorize
        authorize = getUnzer(Keys.MARKETPLACE_KEY).fetchMarketplaceAuthorization(authorize.getPayment().getId(), authorize.getId());
        assertNotNull(authorize.getId());
        assertNotNull(authorize);
        assertEquals(BaseTransaction.Status.SUCCESS, authorize.getStatus());
        assertEquals(participantId_2, authorize.getProcessing().getParticipantId());
    }
}
