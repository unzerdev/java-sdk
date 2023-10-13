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
 * Prepayment business object
 *
 * @author Unzer E-Com GmbH
 */
public class Prepayment extends AbstractPaymentType implements PaymentType {

  @Override
  public String getTypeUrl() {
    return "types/prepayment";
  }

  @Override
  public PaymentType map(PaymentType prepayment, ApiObject jsonId) {
    ((Prepayment) prepayment).setId(jsonId.getId());
    ((Prepayment) prepayment).setRecurring(((ApiIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((Prepayment) prepayment).setGeoLocation(tempGeoLocation);
    return prepayment;
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return charge(amount, currency, returnUrl, null);
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl, customer);
  }

}
