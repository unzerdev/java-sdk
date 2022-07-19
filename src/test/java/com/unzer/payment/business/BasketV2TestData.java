package com.unzer.payment.business;

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
