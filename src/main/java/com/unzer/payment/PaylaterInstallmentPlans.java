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

package com.unzer.payment;

import com.unzer.payment.communication.json.paylater.JsonInstallmentPlan;
import com.unzer.payment.communication.json.paylater.JsonInstallmentPlanRate;
import com.unzer.payment.communication.json.paylater.JsonInstallmentPlans;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlanRate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class PaylaterInstallmentPlans {

  private String id;

  private BigDecimal amount;
  private Currency currency;
  private List<InstallmentPlan> plans = new ArrayList<>();
  private AbstractTransaction.Status status;

  public BigDecimal getAmount() {
    return amount;
  }

  public PaylaterInstallmentPlans setAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public Currency getCurrency() {
    return currency;
  }

  public PaylaterInstallmentPlans setCurrency(Currency currency) {
    this.currency = currency;
    return this;
  }

  public List<InstallmentPlan> getPlans() {
    return plans;
  }

  public PaylaterInstallmentPlans setPlans(List<InstallmentPlan> plans) {
    this.plans = plans;
    return this;
  }

  public String getTypeUrl() {
    return "types/paylater-installment/plans";
  }

  public String getId() {
    return id;
  }

  public PaylaterInstallmentPlans setId(String id) {
    this.id = id;
    return this;
  }

  public AbstractTransaction.Status getStatus() {
    return status;
  }

  public PaylaterInstallmentPlans setStatus(AbstractTransaction.Status status) {
    this.status = status;
    return this;
  }

  public PaylaterInstallmentPlans map(PaylaterInstallmentPlans installmentPlans,
                                      JsonInstallmentPlans jsonInstallmentPlans) {
    if (installmentPlans != null && jsonInstallmentPlans != null) {
      installmentPlans.setId((jsonInstallmentPlans).getId())
          .setAmount(jsonInstallmentPlans.getAmount())
          .setCurrency(jsonInstallmentPlans.getCurrency());

      map(installmentPlans.getPlans(), jsonInstallmentPlans.getPlans());
    }
    return installmentPlans;
  }

  private void map(List<InstallmentPlan> installmentPlan,
                   List<JsonInstallmentPlan> jsonInstallmentPlan) {
    jsonInstallmentPlan.forEach((plan) -> {
      List<InstallmentPlanRate> planRates = new ArrayList<InstallmentPlanRate>();
      mapRates(planRates, plan.getInstallmentRates());
      installmentPlan.add(new InstallmentPlan().setTotalAmount(plan.getTotalAmount())
          .setNumberOfRates(plan.getNumberOfRates())
          .setNominalInterestRate(plan.getNominalInterestRate())
          .setEffectiveInterestRate(plan.getEffectiveInterestRate()).setInstallmentRates(planRates)
          .setSecciUrl(plan.getSecciUrl()));
    });
  }

  private void mapRates(List<InstallmentPlanRate> installmentPlanRates,
                        List<JsonInstallmentPlanRate> json) {
    json.forEach((rate) -> {
      installmentPlanRates.add(
          new InstallmentPlanRate().setRate(rate.getRate()).setDate(rate.getDate()));
    });
  }
}
