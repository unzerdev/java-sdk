package com.unzer.payment.communication.json;

import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;

import java.util.ArrayList;
import java.util.List;

public class JsonInstallmentSecuredRatePlanList {
    private String code;
    private List<InstallmentSecuredRatePlan> entity = new ArrayList<InstallmentSecuredRatePlan>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<InstallmentSecuredRatePlan> getEntity() {
        return entity;
    }

    public void setEntity(List<InstallmentSecuredRatePlan> entity) {
        this.entity = entity;
    }
}
