package com.unzer.payment.models.paylater;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

public class InstallmentPlan {
  private BigDecimal totalAmount;
  private Integer numberOfRates;
  private BigDecimal nominalInterestRate;
  private BigDecimal effectiveInterestRate;
  private List<InstallmentPlanRate> installmentRates;
  private URL secciUrl;

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

  public URL getSecciUrl() {
    return secciUrl;
  }

  public InstallmentPlan setSecciUrl(URL secciUrl) {
    this.secciUrl = secciUrl;
    return this;
  }
}
