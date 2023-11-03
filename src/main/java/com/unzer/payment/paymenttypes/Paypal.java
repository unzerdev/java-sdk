package com.unzer.payment.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.communication.json.ApiPaypal;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Paypal business object
 *
 * @author Unzer E-Com GmbH
 */
public class Paypal extends BasePaymentType {

  private String email;

  @Override
  public String getResourceUrl() {
    return "/v1/types/paypal/<resourceId>";
  }

  @Override
  public PaymentType map(PaymentType paypal, ApiObject jsonId) {
    ((Paypal) paypal).setId(jsonId.getId());
    ((Paypal) paypal).setRecurring(((ApiIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((Paypal) paypal).setGeoLocation(tempGeoLocation);
    ((Paypal) paypal).setRecurring(((ApiPaypal) jsonId).getRecurring());
    ((Paypal) paypal).setEmail(((ApiPaypal) jsonId).getEmail());
    return paypal;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return authorize(amount, currency, returnUrl, null);
  }

  public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl,
                                 Customer customer) throws HttpCommunicationException {
    return getUnzer().authorize(amount, currency, this, returnUrl, customer);
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
