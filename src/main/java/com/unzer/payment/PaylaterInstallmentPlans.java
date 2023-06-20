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
import java.util.List;

public class PaylaterInstallmentPlans {

  private String id;

  private BigDecimal amount;
  private String currency;
  private List<InstallmentPlan> plans = new ArrayList<>();
  private boolean error;
  private boolean success;
  private boolean pending;
  private boolean resumed;

  public BigDecimal getAmount() {
    return amount;
  }

  public PaylaterInstallmentPlans setAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public String getCurrency() {
    return currency;
  }

  public PaylaterInstallmentPlans setCurrency(String currency) {
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

  public boolean isError() {
    return error;
  }

  public PaylaterInstallmentPlans setError(boolean error) {
    this.error = error;
    return this;
  }

  public boolean isSuccess() {
    return success;
  }

  public PaylaterInstallmentPlans setSuccess(boolean success) {
    this.success = success;
    return this;
  }

  public boolean isPending() {
    return pending;
  }

  public PaylaterInstallmentPlans setPending(boolean pending) {
    this.pending = pending;
    return this;
  }

  public boolean isResumed() {
    return resumed;
  }

  public PaylaterInstallmentPlans setResumed(boolean resumed) {
    this.resumed = resumed;
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

  public PaylaterInstallmentPlans map(PaylaterInstallmentPlans installmentPlans,
                                      JsonInstallmentPlans jsonInstallmentPlans) {
    if (installmentPlans != null && jsonInstallmentPlans != null) {
      installmentPlans.setId((jsonInstallmentPlans).getId());
      installmentPlans.setAmount(jsonInstallmentPlans.getAmount());
      installmentPlans.setCurrency(jsonInstallmentPlans.getCurrency());
      installmentPlans.setError(jsonInstallmentPlans.isError());
      installmentPlans.setSuccess(jsonInstallmentPlans.isSuccess());
      installmentPlans.setPending(jsonInstallmentPlans.isPending());
      installmentPlans.setResumed(jsonInstallmentPlans.isResumed());

      map(installmentPlans.getPlans(), jsonInstallmentPlans.getPlans());
//            ((PaylaterInstallmentPlans) installmentPlans).setPlans(jsonInstallmentPlans.getPlans());
    }
    return installmentPlans;
  }

  private void map(List<InstallmentPlan> installmentPlan,
                   List<JsonInstallmentPlan> jsonInstallmentPlan) {
    jsonInstallmentPlan.forEach((plan) -> {
      List<InstallmentPlanRate> planRates = new ArrayList<InstallmentPlanRate>();
      mapRates(planRates, plan.getInstallmentRates());
      installmentPlan.add(
          new InstallmentPlan()
              .setTotalAmount(plan.getTotalAmount())
              .setNumberOfRates(plan.getNumberOfRates())
              .setNominalInterestRate(plan.getNominalInterestRate())
              .setEffectiveInterestRate(plan.getEffectiveInterestRate())
              .setInstallmentRates(planRates)
              .setSecciUrl(plan.getSecciUrl())
      );
    });
  }

  private void mapRates(List<InstallmentPlanRate> installmentPlanRates,
                        List<JsonInstallmentPlanRate> json) {
    json.forEach((rate) -> {
          installmentPlanRates.add(
              new InstallmentPlanRate()
                  .setRate(rate.getRate())
                  .setDate(rate.getDate())
          );
        }
    );
  }
}
