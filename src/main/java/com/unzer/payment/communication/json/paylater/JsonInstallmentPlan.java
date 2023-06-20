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

package com.unzer.payment.communication.json.paylater;

import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

public class JsonInstallmentPlan extends JsonIdObject implements JsonObject {

  private BigDecimal totalAmount;
  private int numberOfRates;
  private BigDecimal nominalInterestRate;
  private BigDecimal effectiveInterestRate;
  private List<JsonInstallmentPlanRate> installmentRates;
  private URL secciUrl;
  private BigDecimal minimumInstallmentFee;

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public JsonInstallmentPlan setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  public int getNumberOfRates() {
    return numberOfRates;
  }

  public JsonInstallmentPlan setNumberOfRates(int numberOfRates) {
    this.numberOfRates = numberOfRates;
    return this;
  }

  public BigDecimal getNominalInterestRate() {
    return nominalInterestRate;
  }

  public JsonInstallmentPlan setNominalInterestRate(BigDecimal nominalInterestRate) {
    this.nominalInterestRate = nominalInterestRate;
    return this;
  }

  public BigDecimal getEffectiveInterestRate() {
    return effectiveInterestRate;
  }

  public JsonInstallmentPlan setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
    this.effectiveInterestRate = effectiveInterestRate;
    return this;
  }

  public List<JsonInstallmentPlanRate> getInstallmentRates() {
    return installmentRates;
  }

  public JsonInstallmentPlan setInstallmentRates(List<JsonInstallmentPlanRate> installmentRates) {
    this.installmentRates = installmentRates;
    return this;
  }

  public URL getSecciUrl() {
    return secciUrl;
  }

  public JsonInstallmentPlan setSecciUrl(URL secciUrl) {
    this.secciUrl = secciUrl;
    return this;
  }

  public BigDecimal getMinimumInstallmentFee() {
    return minimumInstallmentFee;
  }

  public JsonInstallmentPlan setMinimumInstallmentFee(BigDecimal minimumInstallmentFee) {
    this.minimumInstallmentFee = minimumInstallmentFee;
    return this;
  }
}
