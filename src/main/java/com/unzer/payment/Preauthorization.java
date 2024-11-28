package com.unzer.payment;

/**
 * Business object for Pre-Authorization. Amount, currency and typeId are mandatory parameter to
 * execute a Pre-Authorization.
 */
public class Preauthorization extends Authorization {

    public Preauthorization() {
        super();
    }

    @Override
    protected String getTransactionUrl() {
        return "/v1/payments/<paymentId>/preauthorize/<transactionId>";
    }
}
