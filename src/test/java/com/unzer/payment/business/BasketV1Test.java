package com.unzer.payment.business;


import com.unzer.payment.Authorization;
import com.unzer.payment.Basket;
import com.unzer.payment.BasketItem;
import com.unzer.payment.Charge;
import com.unzer.payment.Payment;
import com.unzer.payment.PaymentException;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.service.UrlUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.List;

import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.business.BasketV1TestData.getMinTestBasketV1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasketV1Test extends AbstractPaymentTest {

    @Test
    void testCreateFetchBasket() {
        Basket maxBasket = getMaxTestBasketV1();
        int basketItemCnt = maxBasket.getBasketItems().size();
        for (int i = 0; i < basketItemCnt; i++) {
            maxBasket.getBasketItems().get(i).setParticipantId("testparticipant" + (i + 1));
        }

        Basket basket = getUnzer().createBasket(maxBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(maxBasket, basketFetched);
    }

    private void assertBasketEquals(Basket expected, Basket actual) {
        assertBigDecimalEquals(expected.getAmountTotalGross(), actual.getAmountTotalGross());
        assertBigDecimalEquals(expected.getAmountTotalDiscount(), actual.getAmountTotalDiscount());
        assertBigDecimalEquals(expected.getAmountTotalVat(), actual.getAmountTotalVat());
        assertEquals(expected.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getOrderId(), actual.getOrderId());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertBasketItemsEquals(expected.getBasketItems(), actual.getBasketItems());
    }

    private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        if (expected == null) {
            return;
        }
        expected = expected.setScale(4, RoundingMode.HALF_UP);
        actual = actual.setScale(4, RoundingMode.HALF_UP);
        assertEquals(0, expected.compareTo(actual));
    }

    private void assertBasketItemsEquals(List<BasketItem> expected, List<BasketItem> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertBasketItemEquals(expected.get(i), actual.get(i));
        }
    }

    private void assertBasketItemEquals(BasketItem expected, BasketItem actual) {
        assertBigDecimalEquals(expected.getAmountDiscount(), actual.getAmountDiscount());
        assertBigDecimalEquals(expected.getAmountGross(), actual.getAmountGross());
        assertBigDecimalEquals(expected.getAmountNet(), actual.getAmountNet());
        assertBigDecimalEquals(expected.getAmountPerUnit(), actual.getAmountPerUnit());
        assertBigDecimalEquals(expected.getAmountVat(), actual.getAmountVat());
        assertEquals(expected.getBasketItemReferenceId(), actual.getBasketItemReferenceId());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getUnit(), actual.getUnit());
        assertNumberEquals(expected.getVat(), actual.getVat());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
        assertEquals(expected.getSubTitle(), actual.getSubTitle());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getParticipantId(), actual.getParticipantId());
    }

    @Test
    void testCreateFetchMinBasket() {
        Basket minBasket = getMinTestBasketV1();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(minBasket, basketFetched);
    }

    @Test
    void testUpdateBasket() {
        Basket minBasket = getMinTestBasketV1();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        Basket maxBasket = getMaxTestBasketV1();
        maxBasket.setOrderId(basket.getOrderId());
        Basket updatedBasket = getUnzer().updateBasket(maxBasket, basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(maxBasket, updatedBasket);
    }

    @Test
    void testUpdateBasketWithFetched() {
        Basket minBasket = getMinTestBasketV1();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        basketFetched.setNote("Something changed");
        Basket updatedBasket = getUnzer().updateBasket(basketFetched, basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(basketFetched, updatedBasket);
    }

    @Test
    void testAuthorizationWithBasket()
            throws MalformedURLException, HttpCommunicationException {
        Basket basket = createMaxTestBasket();
        Authorization authorization =
                getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, null,
                        null, basket.getId());
        authorization.setAmount(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()));
        Authorization authorize = getUnzer().authorize(authorization);
        Payment payment = getUnzer().fetchPayment(authorize.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getAuthorization());
        assertNotNull(payment.getAuthorization().getId());
        assertEquals(basket.getId(), payment.getBasketId());
        assertEquals(basket.getId(), payment.getAuthorization().getBasketId());
    }

    private Basket createMaxTestBasket() throws PaymentException, HttpCommunicationException {
        return getUnzer().createBasket(getMaxTestBasketV1());
    }

    @Test
    void testChargeWithBasket() throws MalformedURLException, HttpCommunicationException {
        Basket basket = createMaxTestBasket();
        Charge chargeReq =
                getCharge(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, null, null,
                        basket.getId(), null);
        chargeReq.setAmount(basket.getAmountTotalGross().subtract(basket.getAmountTotalDiscount()));
        Charge charge = getUnzer().charge(chargeReq);
        Payment payment = getUnzer().fetchPayment(charge.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getCharge(0));
        assertNotNull(payment.getCharge(0).getId());
        assertEquals(basket.getId(), payment.getBasketId());
        assertEquals(basket.getId(), payment.getCharge(0).getBasketId());
    }

    @Test
    void testBasketV2IsPriorVersion() {
        Basket basket = new Basket()
                .setTotalValueGross(BigDecimal.TEN)
                .setAmountTotalGross(BigDecimal.ONE);

        UrlUtil urlUtil = new UrlUtil(Keys.KEY_WITHOUT_3DS);

        String url = urlUtil.getUrl(basket);
        assertTrue(url.startsWith("https://sbx-api.unzer.com/v2/"));
    }
}
