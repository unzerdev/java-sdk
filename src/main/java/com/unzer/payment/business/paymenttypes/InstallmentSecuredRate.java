package com.unzer.payment.business.paymenttypes;

import java.math.BigDecimal;

public class InstallmentSecuredRate {
    private BigDecimal amountOfRepayment;
    private BigDecimal rate;
    private BigDecimal totalRemainingAmount;
    private String type;
    private Integer rateIndex;
    private Boolean ultimo;

    public BigDecimal getAmountOfRepayment() {
        return amountOfRepayment;
    }

    public void setAmountOfRepayment(BigDecimal amountOfRepayment) {
        this.amountOfRepayment = amountOfRepayment;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTotalRemainingAmount() {
        return totalRemainingAmount;
    }

    public void setTotalRemainingAmount(BigDecimal totalRemainingAmount) {
        this.totalRemainingAmount = totalRemainingAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRateIndex() {
        return rateIndex;
    }

    public void setRateIndex(Integer rateIndex) {
        this.rateIndex = rateIndex;
    }

    public Boolean getUltimo() {
        return ultimo;
    }

    public void setUltimo(Boolean ultimo) {
        this.ultimo = ultimo;
    }
}
