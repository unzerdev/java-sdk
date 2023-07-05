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

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiPaylaterInstallment;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

public class PaylaterInstallment extends AbstractPaymentType implements PaymentType {

  private String inquiryId;
  private Integer numberOfRates;
  private String iban;
  private String country;
  private String holder;

  public String getInquiryId() {
    return inquiryId;
  }

  public PaylaterInstallment setInquiryId(String inquiryId) {
    this.inquiryId = inquiryId;
    return this;
  }

  public Integer getNumberOfRates() {
    return numberOfRates;
  }

  public PaylaterInstallment setNumberOfRates(Integer numberOfRates) {
    this.numberOfRates = numberOfRates;
    return this;
  }

  public String getIban() {
    return iban;
  }

  public PaylaterInstallment setIban(String iban) {
    this.iban = iban;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public PaylaterInstallment setCountry(String country) {
    this.country = country;
    return this;
  }

  public String getHolder() {
    return holder;
  }

  public PaylaterInstallment setHolder(String holder) {
    this.holder = holder;
    return this;
  }

  @Override
  public PaymentType map(PaymentType pit, JsonObject jsonPit) {
    ((PaylaterInstallment) pit).setId(jsonPit.getId());
    ((PaylaterInstallment) pit).setInquiryId(((ApiPaylaterInstallment) jsonPit).getInquiryId());
    ((PaylaterInstallment) pit).setNumberOfRates(
        ((ApiPaylaterInstallment) jsonPit).getNumberOfRates());
    ((PaylaterInstallment) pit).setIban(((ApiPaylaterInstallment) jsonPit).getIban());
    ((PaylaterInstallment) pit).setCountry(((ApiPaylaterInstallment) jsonPit).getCountry());
    ((PaylaterInstallment) pit).setHolder(((ApiPaylaterInstallment) jsonPit).getHolder());
    ((PaylaterInstallment) pit).setRecurring(((ApiPaylaterInstallment) jsonPit).getRecurring());
    GeoLocation geoLocation =
        new GeoLocation(((JsonIdObject) jsonPit).getGeoLocation().getClientIp(),
            ((JsonIdObject) jsonPit).getGeoLocation().getCountryIsoA2());
    ((PaylaterInstallment) pit).setGeoLocation(geoLocation);
    return pit;
  }

  @Override
  public String getTypeUrl() {
    return "types/paylater-installment";
  }
}
