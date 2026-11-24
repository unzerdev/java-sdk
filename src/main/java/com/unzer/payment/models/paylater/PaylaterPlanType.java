package com.unzer.payment.models.paylater;

/**
 * Selects which Paylater plans endpoint to query. The segment is the
 * {@code /v1/types/<segment>/plans} path part used by the Payment API.
 */
public enum PaylaterPlanType {
    INSTALLMENT("paylater-installment"),
    INVOICE("paylater-invoice"),
    DIRECT_DEBIT("paylater-direct-debit");

    private final String segment;

    PaylaterPlanType(String segment) {
        this.segment = segment;
    }

    public String getSegment() {
        return segment;
    }
}
