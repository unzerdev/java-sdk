package com.unzer.payment.business;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2022 - today Unzer E-Com GmbH
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

import com.unzer.payment.Basket;
import com.unzer.payment.BasketItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;

public class BasketV1TestData {
    @Deprecated
    public static Basket getMaxTestBasketV1() {
        Basket basket = new Basket();
        basket.setAmountTotalGross(new BigDecimal(380.48));
        basket.setAmountTotalVat(new BigDecimal(380.48 * 0.2).setScale(2, RoundingMode.HALF_UP));
        basket.setAmountTotalDiscount(BigDecimal.TEN);
        basket.setAmountTotalVat(new BigDecimal(5.41));
        basket.setCurrencyCode(Currency.getInstance("EUR"));
        basket.setNote("Mistery shopping");
        basket.setOrderId(generateUuid());
        basket.addBasketItem(getMaxTestBasketItem1V1());
        basket.addBasketItem(getMaxTestBasketItem2V1());
        return basket;
    }

    @Deprecated
    public static Basket getMaxTestBasketV1(BigDecimal amount) {
        Basket basket = new Basket();
        basket.setAmountTotalGross(amount);
        basket.setAmountTotalVat(amount.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP));
        basket.setAmountTotalDiscount(BigDecimal.ZERO);
        basket.setAmountTotalVat(new BigDecimal("7.60"));
        basket.setCurrencyCode(Currency.getInstance("EUR"));
        basket.setNote("Mistery shopping");
        basket.setOrderId(generateUuid());
        basket.addBasketItem(getMaxTestBasketItem1V1());
        basket.addBasketItem(getMaxTestBasketItem2V1());
        return basket;
    }

    // TODO: remove unused
    @Deprecated
    protected Basket getTestBasketV1ForInvoice() {
        Basket basket = new Basket();
        basket.setAmountTotalGross(new BigDecimal(14.49));
        basket.setAmountTotalVat(new BigDecimal(14.49 * 0.2).setScale(2, RoundingMode.HALF_UP));
        basket.setAmountTotalDiscount(BigDecimal.ONE);
        basket.setAmountTotalVat(new BigDecimal(3.41));
        basket.setCurrencyCode(Currency.getInstance("EUR"));
        basket.setNote("Mistery shopping");
        basket.setOrderId(generateUuid());
        basket.addBasketItem(getMaxTestBasketItem1V1());
        return basket;
    }


    @Deprecated
    public static Basket getMinTestBasketV1() {
        return new Basket()
                .setAmountTotalGross(new BigDecimal("500.5"))
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .setAmountTotalVat(BigDecimal.ZERO)
                .setAmountTotalDiscount(BigDecimal.ZERO)
                .addBasketItem(getMinTestBasketItemV1());
    }

    @Deprecated
    private static BasketItem getMaxTestBasketItem1V1() {
        BasketItem basketItem = new BasketItem();
        basketItem.setBasketItemReferenceId("Artikelnummer4711");
        basketItem.setAmountDiscount(BigDecimal.ONE);
        basketItem.setAmountGross(new BigDecimal(14.49));
        basketItem.setAmountNet(new BigDecimal(13.49));
        basketItem.setAmountPerUnit(new BigDecimal(14.49));
        basketItem.setAmountVat(new BigDecimal(1.4));
        basketItem.setQuantity(1);
        basketItem.setTitle("Apple iPhone");
        basketItem.setUnit("Pc.");
        basketItem.setVat(19);
        basketItem.setSubTitle("XS in Red");
        basketItem.setType("goods");
        try {
            basketItem.setImageUrl(new URL("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero"));
        } catch (MalformedURLException e) {
        }
        return basketItem;
    }

    @Deprecated
    private static BasketItem getMaxTestBasketItem2V1() {
        BasketItem basketItem = new BasketItem();
        basketItem.setBasketItemReferenceId("Artikelnummer4712");
        basketItem.setAmountDiscount(BigDecimal.ONE);
        basketItem.setAmountGross(new BigDecimal(365.99));
        basketItem.setAmountNet(new BigDecimal(307.55));
        basketItem.setAmountPerUnit(new BigDecimal(223.66));
        basketItem.setAmountVat(new BigDecimal(58.44));
        basketItem.setQuantity(3);
        basketItem.setTitle("Apple iPad Air");
        basketItem.setUnit("Pc.");
        basketItem.setVat(20);
        basketItem.setSubTitle("Nicht nur Pros brauchen Power.");
        basketItem.setType("goods");
        try {
            basketItem.setImageUrl(new URL("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero"));
        } catch (MalformedURLException e) {
        }
        return basketItem;
    }

    @Deprecated
    private static BasketItem getMinTestBasketItemV1() {
        BasketItem basketItem = new BasketItem()
                .setBasketItemReferenceId("Artikelnummer4711")
                .setQuantity(5)
                .setAmountPerUnit(new BigDecimal("100.1"))
                .setAmountNet(new BigDecimal("420.1"))
                .setVat(BigDecimal.ZERO)
                .setAmountDiscount(BigDecimal.ZERO)
                .setTitle("Apple iPhone");

        basketItem.setAmountGross(basketItem.getAmountPerUnit().multiply(new BigDecimal(basketItem.getQuantity())));
        return basketItem;
    }
}
