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
