package com.unzer.payment.communication.json.paylater;

import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

public class ApiInstallmentPlan extends ApiIdObject implements ApiObject {

  private BigDecimal totalAmount;
  private int numberOfRates;
  private BigDecimal nominalInterestRate;
  private BigDecimal effectiveInterestRate;
  private List<ApiInstallmentPlanRate> installmentRates;
  private URL secciUrl;
  private BigDecimal minimumInstallmentFee;

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public ApiInstallmentPlan setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  public int getNumberOfRates() {
    return numberOfRates;
  }

  public ApiInstallmentPlan setNumberOfRates(int numberOfRates) {
    this.numberOfRates = numberOfRates;
    return this;
  }

  public BigDecimal getNominalInterestRate() {
    return nominalInterestRate;
  }

  public ApiInstallmentPlan setNominalInterestRate(BigDecimal nominalInterestRate) {
    this.nominalInterestRate = nominalInterestRate;
    return this;
  }

  public BigDecimal getEffectiveInterestRate() {
    return effectiveInterestRate;
  }

  public ApiInstallmentPlan setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
    this.effectiveInterestRate = effectiveInterestRate;
    return this;
  }

  public List<ApiInstallmentPlanRate> getInstallmentRates() {
    return installmentRates;
  }

  public ApiInstallmentPlan setInstallmentRates(List<ApiInstallmentPlanRate> installmentRates) {
    this.installmentRates = installmentRates;
    return this;
  }

  public URL getSecciUrl() {
    return secciUrl;
  }

  public ApiInstallmentPlan setSecciUrl(URL secciUrl) {
    this.secciUrl = secciUrl;
    return this;
  }

  public BigDecimal getMinimumInstallmentFee() {
    return minimumInstallmentFee;
  }

  public ApiInstallmentPlan setMinimumInstallmentFee(BigDecimal minimumInstallmentFee) {
    this.minimumInstallmentFee = minimumInstallmentFee;
    return this;
  }
}
