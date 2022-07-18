package com.unzer.payment.business;

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

import com.unzer.payment.*;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasketV2Test extends AbstractPaymentTest {
    @Test
    public void testCreateFetchBasket() throws HttpCommunicationException {
        Basket maxBasket = getMaxTestBasketV2();
        Basket basket = getUnzer().createBasket(maxBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(maxBasket, basketFetched);
    }

    @Test
    public void testCreateFetchMinBasket() throws HttpCommunicationException {
        Basket minBasket = getMinTestBasketV2();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(minBasket, basketFetched);
    }

    @Test
    public void testUpdateBasket() throws HttpCommunicationException {
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
    public void testUpdateBasketWithFetched() throws HttpCommunicationException {
        Basket minBasket = getMinTestBasketV2();
        Basket basket = getUnzer().createBasket(minBasket);
        Basket basketFetched = getUnzer().fetchBasket(basket.getId());
        Basket updatedBasket = getUnzer().updateBasket(basketFetched, basket.getId());
        assertNotNull(basketFetched);
        assertNotNull(basketFetched.getId());
        assertBasketEquals(basketFetched, updatedBasket);
    }

    @Test
    public void testAuthorizationWithBasket() throws MalformedURLException, HttpCommunicationException {
        Basket basket = getUnzer().createBasket(getMaxTestBasketV2());
        Authorization authorization = getAuthorization(createPaymentTypeCard().getId(), null, null, null, basket.getId());
        authorization.setAmount(BigDecimal.valueOf(684.47));
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
        Basket basket = getUnzer().createBasket(getMaxTestBasketV2());
        Charge chargeReq = getCharge(createPaymentTypeCard().getId(), null, null, null, basket.getId(), null);
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
