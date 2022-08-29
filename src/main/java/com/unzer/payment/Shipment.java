package com.unzer.payment;

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

public class Shipment extends AbstractTransaction<Payment> {

    private String invoiceId;

    public Shipment() {
        super();
    }

    public Shipment(String invoiceId, String orderId) {
        super();
        this.invoiceId = invoiceId;
        setOrderId(orderId);
    }

    @Override
    public String getTypeUrl() {
        return "payments/<paymentId>/shipments";
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
}
