package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class PostFinanceCard extends BasePaymentType {

  @Override
  public String getResourceUrl() {
    return "/v1/types/post-finance-card/<resourceId>";
  }

  @Override
  public PaymentType map(PaymentType postFinanceCard, ApiObject jsonId) {
    ((PostFinanceCard) postFinanceCard).setId(jsonId.getId());
    ((PostFinanceCard) postFinanceCard).setRecurring(((ApiIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((PostFinanceCard) postFinanceCard).setGeoLocation(tempGeoLocation);
    return postFinanceCard;
  }

  @Deprecated
  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl);
  }

  @Deprecated
  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl, customer);
  }

}
