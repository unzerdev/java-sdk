package com.unzer.payment;

/**
 * Business object for SCA (Strong Customer Authentication) transaction.
 */
public class Sca extends BaseTransaction<Payment> {

    public Sca() {
        super();
    }

    @Override
    protected String getTransactionUrl() {
        return "/v1/payments/<paymentId>/sca/<transactionId>";
    }
}
