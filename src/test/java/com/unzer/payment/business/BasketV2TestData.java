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
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;

public class BasketV2TestData {
    // TODO: remove unused
    public static Basket getTestBasketV2ForInvoice() {
        return new Basket()
                .setTotalValueGross(new BigDecimal("14.49"))
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .addBasketItem(getMaxTestBasketItem1V2());
    }

    public static Basket getMaxTestBasketV2() {
        return new Basket()
                .setTotalValueGross(new BigDecimal("380.48"))
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .addBasketItem(getMaxTestBasketItem1V2()) // 14.48 - 1.0
                .addBasketItem(getMaxTestBasketItem2V2()); // 366 // 122 * 3
    }

    public static BasketItem getMaxTestBasketItem2V2() {
        return new BasketItem()
                .setBasketItemReferenceId("Artikelnummer4712")
                .setAmountPerUnitGross(new BigDecimal("122"))
                .setQuantity(3)
                .setTitle("Apple iPad Air")
                .setUnit("Pc.")
                .setAmountDiscountPerUnitGross(BigDecimal.ZERO)
                .setVat(20)
                .setSubTitle("Nicht nur Pros brauchen Power.")
                .setType(BasketItem.Type.GOODS)
                .setImageUrl(unsafeUrl("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero"));
    }


    public static BasketItem getMaxTestBasketItem1V2() {
        return new BasketItem()
                .setBasketItemReferenceId("Artikelnummer4711")
                .setAmountPerUnitGross(new BigDecimal("24.48"))
                .setAmountDiscountPerUnitGross(BigDecimal.TEN)
                .setQuantity(1)
                .setTitle("Apple iPhone")
                .setUnit("Pc.")
                .setVat(19)
                .setSubTitle("XS in Red").setType(BasketItem.Type.GOODS)
                .setImageUrl(unsafeUrl("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero"));
    }

    public static Basket getMaxTestBasketV2(BigDecimal amount) {
        return new Basket()
                .setTotalValueGross(amount)
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .addBasketItem(getMaxTestBasketItem1V2())
                .addBasketItem(getMaxTestBasketItem2V2());
    }


    public static BasketItem getMinTestBasketItemV2() {
        return new BasketItem()
                .setBasketItemReferenceId("Artikelnummer4711")
                .setQuantity(5)
                .setVat(0)
                .setAmountDiscountPerUnitGross(BigDecimal.ZERO)
                .setAmountPerUnitGross(new BigDecimal("100.1"))
                .setTitle("Apple iPhone");
    }


    public static Basket getMinTestBasketV2() {
        return new Basket()
                .setTotalValueGross(new BigDecimal("500.5"))
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .addBasketItem(getMinTestBasketItemV2());
    }
}
