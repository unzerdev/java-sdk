package com.unzer.payment;

public class BasketV3 extends Basket {
    @Override
    protected String getResourceUrl() {
        return "/v3/baskets/<resourceId>";
    }
}
