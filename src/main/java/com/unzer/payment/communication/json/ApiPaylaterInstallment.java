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

package com.unzer.payment.communication.json;

public class ApiPaylaterInstallment extends JsonIdObject implements JsonObject {
  private String inquiryId;
  private Integer numberOfRates;
  private String iban;
  private String country;
  private String holder;

  public String getInquiryId() {
    return inquiryId;
  }

  public ApiPaylaterInstallment setInquiryId(String inquiryId) {
    this.inquiryId = inquiryId;
    return this;
  }

  public Integer getNumberOfRates() {
    return numberOfRates;
  }

  public ApiPaylaterInstallment setNumberOfRates(Integer numberOfRates) {
    this.numberOfRates = numberOfRates;
    return this;
  }

  public String getIban() {
    return iban;
  }

  public ApiPaylaterInstallment setIban(String iban) {
    this.iban = iban;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public ApiPaylaterInstallment setCountry(String country) {
    this.country = country;
    return this;
  }

  public String getHolder() {
    return holder;
  }

  public ApiPaylaterInstallment setHolder(String holder) {
    this.holder = holder;
    return this;
  }
}
