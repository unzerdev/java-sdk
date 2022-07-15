package com.unzer.payment.business.v2;

import com.unzer.payment.v2.Basket;
import com.unzer.payment.v2.BasketItem;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BasketTest {
    @Test
    public void testCreateFetchBasket() throws HttpCommunicationException {
        Basket maxBasket = getMaxTestBasket();
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

    @Test
    public void testCreateFetchMinBasket() throws HttpCommunicationException {
        Basket minBasket = getMinTestBasket();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(minBasket, basketFetched);
    }

    @Test
    public void testUpdateBasket() throws HttpCommunicationException {
        Basket minBasket = getMinTestBasket();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        Basket maxBasket = getMaxTestBasket();
        maxBasket.setOrderId(basket.getOrderId());
        Basket updatedBasket = getUnzer().updateBasket(maxBasket, basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(maxBasket, updatedBasket);
    }

    @Test
    public void testUpdateBasketWithFetched() throws HttpCommunicationException {
        Basket minBasket = getMinTestBasket();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        basketFetched.setNote("Something changed");
        Basket updatedBasket = getUnzer().updateBasket(basketFetched, basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(basketFetched, updatedBasket);
    }

    @Test
    public void testAuthorizationWithBasket() throws MalformedURLException, HttpCommunicationException {
        Basket basket = createMaxTestBasket();
        Authorization authorization = getAuthorization(createPaymentTypeCard().getId(), null, null, null, basket.getId());
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

    @Test
    public void testChargeWithBasket() throws MalformedURLException, HttpCommunicationException {
        Basket basket = createMaxTestBasket();
        Charge chargeReq = getCharge(createPaymentTypeCard().getId(), null, null, null, basket.getId(), null);
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

    private Basket createMaxTestBasket() throws PaymentException, HttpCommunicationException {
        return getUnzer().createBasket(getMaxTestBasket());
    }


    private void assertBasketEquals(Basket expected, Basket actual) {
        assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }

    private Basket getMaxTestBasket() {
        return getMaxTestBasket(BigDecimal.valueOf(380.48));
    }

    protected Basket getMaxTestBasket(BigDecimal amount) {
        return Basket.Builder.create(
                        amount,
                        Currency.getInstance("EUR")
                ).basketItem(getMaxTestBasketItem1())
                .basketItem(getMaxTestBasketItem2())
                .note("Mistery shopping")
                .orderId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    private BasketItem getMaxTestBasketItem1() {
        return BasketItem.Builder.create(
                        "Apple iPhone",
                        1,
                        BigDecimal.valueOf(14.49),
                        BigDecimal.valueOf(19)
                ).basketItemReferenceId("Artikelnummer4711")
                .amountDiscountPerUnitGross(BigDecimal.ONE)
                .subTitle("XS in Red")
                .unit("Pc.")
                .type(BasketItem.Type.GOODS)
                .imageUrl(unsafeUrl("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero"))
                .build();
    }

    private BasketItem getMaxTestBasketItem2() {
        return BasketItem.Builder.create(
                        "Apple iPad Air",
                        3,
                        BigDecimal.valueOf(223.66),
                        BigDecimal.valueOf(20)
                ).basketItemReferenceId("Artikelnummer4712")
                .amountDiscountPerUnitGross(BigDecimal.ONE)
                .subTitle("Nicht nur Pros brauchen Power.")
                .unit("Pc.")
                .type(BasketItem.Type.GOODS)
                .imageUrl(unsafeUrl("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero"))
                .build();
    }

    private URL unsafeUrl(String resource) {
        try {
            return new URL(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
