package com.unzer.payment;

/**
 * This is a prototype of the v3 Basket resource.
 * <p>
 * This class represents version 3 of Basket resource in the Unzer API.
 * The version uses bearer authentication for API calls.
 * Make sure to use the same Unzer instance to use the same JWT token across multiple calls.
 * Also, the resource ID incorporates UUID and has a length of 42.
 */
public class BasketV3 extends Basket {
    @Override
    protected String getResourceUrl() {
        return "/v3/baskets/<resourceId>";
    }
}
