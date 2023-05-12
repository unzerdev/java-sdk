/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unzer.payment.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.communication.json.JsonPaypal;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Paypal business object
 *
 * @author Unzer E-Com GmbH
 */
public class Paypal extends AbstractPaymentType implements PaymentType {

  private String email;

  @Override
  public String getTypeUrl() {
    return "types/paypal";
  }

  @Override
  public PaymentType map(PaymentType paypal, JsonObject jsonId) {
    ((Paypal) paypal).setId(jsonId.getId());
    ((Paypal) paypal).setRecurring(((JsonIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(),
            ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((Paypal) paypal).setGeoLocation(tempGeoLocation);
    ((Paypal) paypal).setRecurring(((JsonPaypal) jsonId).getRecurring());
    ((Paypal) paypal).setEmail(((JsonPaypal) jsonId).getEmail());
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

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl);
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl, customer);
  }

}
