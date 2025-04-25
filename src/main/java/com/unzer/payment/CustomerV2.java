package com.unzer.payment;

/**
 * This is a prototype of the v2 Customer resource.
 *
 * This class represents version 2 of Customer resource in the Unzer API.
 * The version uses bearer authentication for API calls.
 * Make sure to use the same Unzer instance to use the same JWT token across multiple calls.
 * Also, the resource ID incorporates UUID and has a length of 42.
 */
public class CustomerV2 extends Customer {
    public CustomerV2(String firstname, String lastname) {
        super(firstname, lastname);
    }

    public CustomerV2(String company) {
        super(company);
    }

    @Override
    public String getResourceUrl() {
        return "/v2/customers/<resourceId>";
    }
}
