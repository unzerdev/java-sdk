package com.unzer.payment;

public class Payout extends BaseTransaction<Payment> {
    public Payout() {
    }

    @Deprecated
    public Payout(Unzer unzer) {
        super(unzer);
    }

    @Override
    public String getTransactionUrl() {
        return "/v1/payments/<paymentId>/payouts/<transactionId>";
    }
}
