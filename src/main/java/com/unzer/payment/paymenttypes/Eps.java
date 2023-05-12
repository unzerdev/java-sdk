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

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.GeoLocation;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class Eps extends AbstractPaymentType implements PaymentType {

  private String bic;

  public Eps(String bic) {
    this.bic = bic;
  }

  public Eps() {
    super();
  }

  public Eps(Unzer unzer) {
    super(unzer);
  }

  @Override
  public String getTypeUrl() {
    return "types/eps";
  }

  @Override
  public PaymentType map(PaymentType eps, JsonObject jsonId) {
    ((Eps) eps).setId(jsonId.getId());
    ((Eps) eps).setRecurring(((JsonIdObject) jsonId).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((JsonIdObject) jsonId).getGeoLocation().getClientIp(),
            ((JsonIdObject) jsonId).getGeoLocation().getCountryIsoA2());
    ((Eps) eps).setGeoLocation(tempGeoLocation);
    return eps;
  }

  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
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
