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

/**
 * Giropay business object
 *
 * @author Unzer E-Com GmbH
 */
public class Giropay extends AbstractPaymentType implements PaymentType {

  @Override
  public String getTypeUrl() {
    return "types/giropay";
  }

  @Override
  public PaymentType map(PaymentType giropay, ApiObject jsonId) {
    ((Giropay) giropay).setId(jsonId.getId());
    ((Giropay) giropay).setRecurring(((ApiIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((Giropay) giropay).setGeoLocation(tempGeoLocation);
    return giropay;
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl);
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl, customer);
  }

}
