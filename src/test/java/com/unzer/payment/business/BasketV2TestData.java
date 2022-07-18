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
                .setTotalValueGross(BigDecimal.valueOf(14.49))
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .addBasketItem(getMaxTestBasketItem1V2());
    }

    public static Basket getMaxTestBasketV2() {
        return new Basket()
                .setTotalValueGross(BigDecimal.valueOf(684.47))
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .addBasketItem(getMaxTestBasketItem1V2()) // 14.49 - 1.0
                .addBasketItem(getMaxTestBasketItem2V2()); // 223.66
    }

    public static BasketItem getMaxTestBasketItem2V2() {
        return new BasketItem()
                .setBasketItemReferenceId("Artikelnummer4712")
                .setAmountPerUnitGross(BigDecimal.valueOf(223.66))
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
                .setAmountPerUnitGross(BigDecimal.valueOf(14.49))
                .setAmountDiscountPerUnitGross(BigDecimal.ONE)
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
                .setAmountPerUnitGross(BigDecimal.valueOf(100.1))
                .setTitle("Apple iPhone");
    }


    public static Basket getMinTestBasketV2() {
        return new Basket()
                .setTotalValueGross(BigDecimal.valueOf(500.5))
                .setCurrencyCode(Currency.getInstance("EUR"))
                .setOrderId(generateUuid())
                .addBasketItem(getMinTestBasketItemV2());
    }
}
