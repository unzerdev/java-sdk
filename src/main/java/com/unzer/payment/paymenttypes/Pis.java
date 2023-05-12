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
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.communication.json.JsonPis;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Paypal business object
 *
 * @author Unzer E-Com GmbH
 */
public class Pis extends AbstractPaymentType implements PaymentType {

  private String iban;
  private String bic;
  private String holder;

  public Pis() {
    super();
  }

  public Pis(String iban) {
    super();
    this.iban = iban;
  }

  public Pis(String iban, String bic) {
    super();
    this.iban = iban;
    this.bic = bic;
  }

  @Override
  public String getTypeUrl() {
    return "types/pis";
  }

  @Override
  public PaymentType map(PaymentType pis, JsonObject jsonPis) {
    ((Pis) pis).setId(jsonPis.getId());
    ((Pis) pis).setRecurring(((JsonPis) jsonPis).getRecurring());
    ((Pis) pis).setBic(((JsonPis) jsonPis).getBic());
    ((Pis) pis).setIban(((JsonPis) jsonPis).getIban());
    ((Pis) pis).setHolder(((JsonPis) jsonPis).getHolder());
    GeoLocation tempGeoLocation =
        new GeoLocation(((JsonPis) jsonPis).getGeoLocation().getClientIp(),
            ((JsonPis) jsonPis).getGeoLocation().getCountryIsoA2());
    ((Pis) pis).setGeoLocation(tempGeoLocation);
    return pis;
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl);
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl, customer);
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public String getHolder() {
    return holder;
  }

  public void setHolder(String holder) {
    this.holder = holder;
  }

}
