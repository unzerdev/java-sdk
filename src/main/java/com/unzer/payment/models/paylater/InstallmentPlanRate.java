package com.unzer.payment.models.paylater;

import java.math.BigDecimal;
import java.util.Date;

public class InstallmentPlanRate {
    private Date date;
    private BigDecimal rate;

    public Date getDate() {
        return date;
    }

    public InstallmentPlanRate setDate(Date date) {
        this.date = date;
        return this;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public InstallmentPlanRate setRate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }
}
