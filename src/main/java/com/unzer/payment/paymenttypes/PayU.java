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
 * PayU payment type
 *
 * @author Unzer E-Com GmbH
 */
public class PayU extends AbstractPaymentType implements PaymentType {

  @Override
  public String getTypeUrl() {
    return "types/payu";
  }

  @Override
  public PaymentType map(PaymentType payu, ApiObject jsonId) {
    ((PayU) payu).setId(jsonId.getId());
    ((PayU) payu).setRecurring(((ApiIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) jsonId).getGeoLocation().getClientIp(),
            ((ApiIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((PayU) payu).setGeoLocation(tempGeoLocation);
    return payu;
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
