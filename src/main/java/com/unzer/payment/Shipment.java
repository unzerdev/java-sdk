package com.unzer.payment;

public class Shipment extends BaseTransaction<Payment> {

    public Shipment() {
    }

    public Shipment(String invoiceId, String orderId) {
        setOrderId(orderId);
        setInvoiceId(invoiceId);
    }

    @Override
    public String getTransactionUrl() {
        return "/v1/payments/<paymentId>/shipments/<transactionId>";
    }
}
