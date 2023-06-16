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
import java.util.List;

public class JsonInstallmentPlans extends JsonIdObject implements JsonObject {
    private BigDecimal amount;
    private String currency;
    private List<JsonInstallmentPlan> plans;
    private boolean isError;
    private boolean isSuccess;
    private boolean isPending;
    private boolean isResumed;

    public BigDecimal getAmount() {
        return amount;
    }

    public JsonInstallmentPlans setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public JsonInstallmentPlans setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public List<JsonInstallmentPlan> getPlans() {
        return plans;
    }

    public JsonInstallmentPlans setPlans(List<JsonInstallmentPlan> plans) {
        this.plans = plans;
        return this;
    }

    public boolean isError() {
        return isError;
    }

    public JsonInstallmentPlans setError(boolean error) {
        isError = error;
        return this;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public JsonInstallmentPlans setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public boolean isPending() {
        return isPending;
    }

    public JsonInstallmentPlans setPending(boolean pending) {
        isPending = pending;
        return this;
    }

    public boolean isResumed() {
        return isResumed;
    }

    public JsonInstallmentPlans setResumed(boolean resumed) {
        isResumed = resumed;
        return this;
    }
}
