package com.unzer.payment.paymenttypes;

import com.unzer.payment.Resource;
import com.unzer.payment.communication.json.ApiObject;

public interface PaymentType extends Resource {
  @Deprecated
  String getTypeUrl();

  String getId();

  PaymentType map(PaymentType paymentType, ApiObject apiObject);
}
