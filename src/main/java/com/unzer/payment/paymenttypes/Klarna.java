package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

public class Klarna extends AbstractPaymentType {
  @Override
  public String getTypeUrl() {
    return "types/klarna";
  }

  @Override
  public PaymentType map(PaymentType paymentType, ApiObject apiObject) {
    ((Klarna) paymentType).setId(apiObject.getId());
    ((Klarna) paymentType).setRecurring(((ApiIdObject) apiObject).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) apiObject).getGeoLocation().getClientIp(),
            ((ApiIdObject) apiObject).getGeoLocation().getCountryIsoA2());
    ((Klarna) paymentType).setGeoLocation(tempGeoLocation);

    return paymentType;
  }
}
