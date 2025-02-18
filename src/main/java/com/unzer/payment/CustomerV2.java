package com.unzer.payment;

/**
 * Business object for Customer together with billingAddress.
 * <p>
 * firstname and lastname are mandatory to create a new Customer.
 *
 * @author Unzer E-Com GmbH
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
