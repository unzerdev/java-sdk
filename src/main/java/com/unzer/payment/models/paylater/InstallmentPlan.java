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
package com.unzer.payment.models.paylater;

import java.math.BigDecimal;
import java.util.List;

public class InstallmentPlan {
    private BigDecimal totalAmount;
    private Integer numberOfRates;
    private BigDecimal nominalInterestRate;
    private BigDecimal effectiveInterestRate;
    private List<InstallmentPlanRate> installmentRates;
    private String secciUrl; // TODO: use URL object instead?

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public InstallmentPlan setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public Integer getNumberOfRates() {
        return numberOfRates;
    }

    public InstallmentPlan setNumberOfRates(Integer numberOfRates) {
        this.numberOfRates = numberOfRates;
        return this;
    }

    public BigDecimal getNominalInterestRate() {
        return nominalInterestRate;
    }

    public InstallmentPlan setNominalInterestRate(BigDecimal nominalInterestRate) {
        this.nominalInterestRate = nominalInterestRate;
        return this;
    }

    public BigDecimal getEffectiveInterestRate() {
        return effectiveInterestRate;
    }

    public InstallmentPlan setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
        this.effectiveInterestRate = effectiveInterestRate;
        return this;
    }

    public List<InstallmentPlanRate> getInstallmentRates() {
        return installmentRates;
    }

    public InstallmentPlan setInstallmentRates(List<InstallmentPlanRate> installmentRates) {
        this.installmentRates = installmentRates;
        return this;
    }

    public String getSecciUrl() {
        return secciUrl;
    }

    public InstallmentPlan setSecciUrl(String secciUrl) {
        this.secciUrl = secciUrl;
        return this;
    }
}
