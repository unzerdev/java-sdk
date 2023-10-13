package com.unzer.payment;

import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.PaymentType;

public class Shipment extends AbstractTransaction<Payment> {

  public Shipment() {
    super();
  }

  public Shipment(String invoiceId, String orderId) {
    super();
    setOrderId(orderId);
    setInvoiceId(invoiceId);
  }

  @Override
  public String getTypeUrl() {
    return "payments/<paymentId>/shipments";
  }

  @Override
  public PaymentType map(PaymentType paymentType, ApiObject apiObject) {
    return null;
  }
}
