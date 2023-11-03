package com.unzer.payment.communication.json.paylater;

import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

import java.math.BigDecimal;
import java.util.Date;

public class ApiInstallmentPlanRate extends ApiIdObject implements ApiObject {

    private Date date;
    private BigDecimal rate;

    public Date getDate() {
        return date;
    }

    public ApiInstallmentPlanRate setDate(Date date) {
        this.date = date;
        return this;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public ApiInstallmentPlanRate setRate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }
}
