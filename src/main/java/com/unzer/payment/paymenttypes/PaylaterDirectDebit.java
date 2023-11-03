package com.unzer.payment.paymenttypes;

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.communication.json.ApiSepaDirectDebit;

public class PaylaterDirectDebit extends BasePaymentType {
  private String iban;
  private String holder;

  public PaylaterDirectDebit() {
  }

  public PaylaterDirectDebit(String iban, String holder) {
    this.iban = iban;
    this.holder = holder;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getHolder() {
    return holder;
  }

  public void setHolder(String holder) {
    this.holder = holder;
  }

  @Override
  public String getResourceUrl() {
    return "/v1/types/paylater-direct-debit/<resourceId>";
  }

  @Override
  public PaymentType map(PaymentType type, ApiObject apiObject) {
    ((PaylaterDirectDebit) type).setId(apiObject.getId());
    ((PaylaterDirectDebit) type).setIban(((ApiSepaDirectDebit) apiObject).getIban());
    ((PaylaterDirectDebit) type).setHolder(((ApiSepaDirectDebit) apiObject).getHolder());
    ((PaylaterDirectDebit) type).setRecurring(((ApiSepaDirectDebit) apiObject).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiSepaDirectDebit) apiObject).getGeoLocation().getClientIp(),
            ((ApiSepaDirectDebit) apiObject).getGeoLocation().getCountryIsoA2());
    ((PaylaterDirectDebit) type).setGeoLocation(tempGeoLocation);

    return type;
  }
}
