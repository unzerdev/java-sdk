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
 * Alipay business object
 *
 * @author Unzer E-Com GmbH
 */
public class Alipay extends AbstractPaymentType implements PaymentType {

  @Override
  public String getTypeUrl() {
    return "types/alipay";
  }

  @Override
  public PaymentType map(PaymentType alipay, ApiObject jsonId) {
    ((Alipay) alipay).setId(jsonId.getId());
    ((Alipay) alipay).setRecurring(((ApiIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((Alipay) alipay).setGeoLocation(tempGeoLocation);
    return alipay;
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
