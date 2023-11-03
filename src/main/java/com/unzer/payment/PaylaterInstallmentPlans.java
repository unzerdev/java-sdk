package com.unzer.payment;

import com.unzer.payment.communication.json.paylater.ApiInstallmentPlan;
import com.unzer.payment.communication.json.paylater.ApiInstallmentPlanRate;
import com.unzer.payment.communication.json.paylater.ApiInstallmentPlans;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlanRate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class PaylaterInstallmentPlans {

    private String id;

    private BigDecimal amount;
    private Currency currency;
    private List<InstallmentPlan> plans = new ArrayList<>();
    private BaseTransaction.Status status;

    public BigDecimal getAmount() {
        return amount;
    }

    public PaylaterInstallmentPlans setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PaylaterInstallmentPlans setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public List<InstallmentPlan> getPlans() {
        return plans;
    }

    public PaylaterInstallmentPlans setPlans(List<InstallmentPlan> plans) {
        this.plans = plans;
        return this;
    }

    public String getTypeUrl() {
        return "types/paylater-installment/plans";
    }

    public String getId() {
        return id;
    }

    public PaylaterInstallmentPlans setId(String id) {
        this.id = id;
        return this;
    }

    public BaseTransaction.Status getStatus() {
        return status;
    }

    public PaylaterInstallmentPlans setStatus(BaseTransaction.Status status) {
        this.status = status;
        return this;
    }

    public PaylaterInstallmentPlans map(PaylaterInstallmentPlans installmentPlans,
                                        ApiInstallmentPlans jsonInstallmentPlans) {
        if (installmentPlans != null && jsonInstallmentPlans != null) {
            installmentPlans.setId((jsonInstallmentPlans).getId())
                    .setAmount(jsonInstallmentPlans.getAmount())
                    .setCurrency(jsonInstallmentPlans.getCurrency());

            map(installmentPlans.getPlans(), jsonInstallmentPlans.getPlans());
        }
        return installmentPlans;
    }

    private void map(List<InstallmentPlan> installmentPlan,
                     List<ApiInstallmentPlan> jsonInstallmentPlan) {
        jsonInstallmentPlan.forEach((plan) -> {
            List<InstallmentPlanRate> planRates = new ArrayList<InstallmentPlanRate>();
            mapRates(planRates, plan.getInstallmentRates());
            installmentPlan.add(new InstallmentPlan().setTotalAmount(plan.getTotalAmount())
                    .setNumberOfRates(plan.getNumberOfRates())
                    .setNominalInterestRate(plan.getNominalInterestRate())
                    .setEffectiveInterestRate(plan.getEffectiveInterestRate()).setInstallmentRates(planRates)
                    .setSecciUrl(plan.getSecciUrl()));
        });
    }

    private void mapRates(List<InstallmentPlanRate> installmentPlanRates,
                          List<ApiInstallmentPlanRate> json) {
        json.forEach((rate) -> {
            installmentPlanRates.add(
                    new InstallmentPlanRate().setRate(rate.getRate()).setDate(rate.getDate()));
        });
    }
}
