package com.unzer.payment.business;


import com.unzer.payment.*;
import com.unzer.payment.communication.JsonParser;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.unzer.payment.business.BasketV2TestData.getMaxTestBasketV2;
import static com.unzer.payment.business.BasketV2TestData.getMinTestBasketV2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BasketV2Test extends AbstractPaymentTest {
    @Test
    public void testCreateFetchBasket() {
        Basket maxBasket = getMaxTestBasketV2();
        Basket basket = getUnzer().createBasket(maxBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(maxBasket, basketFetched);
    }

    private void assertBasketEquals(Basket expected, Basket actual) {
        assertThat(expected)
                .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }

    @Test
    public void testCreateFetchMinBasket() {
        Basket minBasket = getMinTestBasketV2();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(minBasket, basketFetched);
    }

    @Test
    public void testUpdateBasket() {
        Basket minBasket = getMinTestBasketV2();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        Basket maxBasket = getMaxTestBasketV2();
        maxBasket.setOrderId(basket.getOrderId());
        Basket updatedBasket = getUnzer().updateBasket(maxBasket, basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());

        maxBasket.setId(basket.getId());
        assertBasketEquals(maxBasket, updatedBasket);
    }

    @Test
    public void testUpdateBasketWithFetched() {
        Basket minBasket = getMinTestBasketV2();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        Basket updatedBasket = getUnzer().updateBasket(basketFetched, basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(basketFetched, updatedBasket);
    }

    @Test
    public void testAuthorizationWithBasket() {
        Unzer unzer = getUnzer();
        Basket basket = unzer.createBasket(getMaxTestBasketV2());
        Authorization authorization = getAuthorization(
                createPaymentTypeCard(unzer, "4711100000000000").getId(),
                null,
                null,
                null,
                basket.getId()
        );
        authorization.setAmount(BigDecimal.valueOf(684.47));
        Authorization authorize = unzer.authorize(authorization);
        Payment payment = unzer.fetchPayment(authorize.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getAuthorization());
        assertNotNull(payment.getAuthorization().getId());
        assertEquals(basket.getId(), payment.getBasketId());
        assertEquals(basket.getId(), payment.getAuthorization().getBasketId());
    }

    @Test
    public void testChargeWithBasket() {
        Basket basket = getUnzer().createBasket(getMaxTestBasketV2());
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

    @Test
    public void testTypeEnumUnmarshalling() {
        List<MapEntry<String, BasketItem>> validTypeValues = Arrays.asList(
                MapEntry.entry(null, new BasketItem()),
                MapEntry.entry("goods", new BasketItem().setType(BasketItem.Type.GOODS)),
                MapEntry.entry("voucher", new BasketItem().setType(BasketItem.Type.VOUCHER)),
                MapEntry.entry("shipment", new BasketItem().setType(BasketItem.Type.SHIPMENT)),
                MapEntry.entry("digital", new BasketItem().setType(BasketItem.Type.DIGITAL)),
                MapEntry.entry("", new BasketItem().setType(BasketItem.Type.EMPTY))
        );

        for (MapEntry<String, BasketItem> entry : validTypeValues) {
            String basketItemJson = String.format("{ \"type\": \"%s\" }", entry.key);

            JsonParser jsonParser = new JsonParser();

            BasketItem basketItem = jsonParser.fromJson(basketItemJson, BasketItem.class);

            assertThat(basketItem)
                    .usingRecursiveComparison()
                    .isEqualTo(entry.value);
        }
    }

    @Test
    public void testInvalidTypeSkipped() {
        String basketItemJson = "{ \"type\": \"invalid-type\" }";

        JsonParser jsonParser = new JsonParser();
        BasketItem basketItem = jsonParser.fromJson(basketItemJson, BasketItem.class);

        assertNull(basketItem.getType());
    }
}
