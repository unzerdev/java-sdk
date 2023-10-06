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

import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.communication.json.TransactionStatus;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class ApiInstallmentPlans extends ApiIdObject implements ApiObject, TransactionStatus {
  private BigDecimal amount;
  private Currency currency;
  private List<ApiInstallmentPlan> plans;
  private boolean isError;
  private boolean isSuccess;
  private boolean isPending;
  private boolean isResumed;
  private TransactionStatus status;

  public BigDecimal getAmount() {
    return amount;
  }

  public ApiInstallmentPlans setAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public Currency getCurrency() {
    return currency;
  }

  public ApiInstallmentPlans setCurrency(Currency currency) {
    this.currency = currency;
    return this;
  }

  public List<ApiInstallmentPlan> getPlans() {
    return plans;
  }

  public ApiInstallmentPlans setPlans(List<ApiInstallmentPlan> plans) {
    this.plans = plans;
    return this;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public ApiInstallmentPlans setStatus(
      TransactionStatus status) {
    this.status = status;
    return this;
  }

  @Override
  public Boolean getSuccess() {
    return this.isSuccess;
  }

  @Override
  public void setSuccess(Boolean value) {
    this.isSuccess = value;
  }

  @Override
  public Boolean getError() {
    return this.isError;

  }

  @Override
  public void setError(Boolean value) {
    this.isError = value;
  }

  @Override
  public Boolean getPending() {
    return this.isPending;

  }

  @Override
  public void setPending(Boolean value) {
    this.isPending = value;
  }

  @Override
  public Boolean getResumed() {
    return this.isResumed;
  }

  @Override
  public void setResumed(Boolean value) {
    this.isResumed = value;
  }
}
