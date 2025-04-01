package com.unzer.payment.business;

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Payment;
import com.unzer.payment.integration.resources.BearerAuthBaseTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.unzer.payment.business.BasketV3TestData.getMaxTestBasketV3;
import static com.unzer.payment.business.BasketV3TestData.getMinTestBasketV3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BasketV3Test extends BearerAuthBaseTest {
    @Test
    void testCreateFetchBasket() {
        Basket maxBasket = getMaxTestBasketV3();
        Basket basket = getUnzer().createBasket(maxBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(maxBasket, basketFetched);
    }

    @Test
    void testCreateFetchMinBasket() {
        Basket minBasket = getMinTestBasketV3();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertBasketEquals(minBasket, basketFetched);
    }

    @Test
    void testUpdateBasket() {
        Basket minBasket = getMinTestBasketV3();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket maxBasket = getMaxTestBasketV3();
        maxBasket.setOrderId(basket.getOrderId());
        Basket updatedBasket = getUnzer().updateBasket(maxBasket, basket.getId());

        maxBasket.setId(basket.getId());
        assertBasketEquals(maxBasket, updatedBasket);
    }

    @Test
    void testChargeWithBasket() {
        Basket basket = getUnzer().createBasket(getMaxTestBasketV3());
        Charge chargeReq =
                getCharge(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, null, null,
                        basket.getId(), null);
        chargeReq.setAmount(BigDecimal.valueOf(684.47));
        Charge charge = getUnzer().charge(chargeReq);
        Payment payment = getUnzer().fetchPayment(charge.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getCharge(0));
        assertNotNull(payment.getCharge(0).getId());
        assertEquals(basket.getId(), payment.getBasketId());
        assertEquals(basket.getId(), payment.getCharge(0).getBasketId());
    }

    private void assertBasketEquals(Basket expected, Basket actual) {
        assertThat(expected)
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }
}
