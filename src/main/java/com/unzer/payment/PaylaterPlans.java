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

/**
 * Available plans returned by the Paylater {@code /plans} endpoints
 * (installment, invoice or direct debit). Each plan carries its SECCI document url.
 */
public class PaylaterPlans {

    private String id;

    private BigDecimal amount;
    private Currency currency;
    private List<InstallmentPlan> plans = new ArrayList<>();
    private BaseTransaction.Status status;

    public BigDecimal getAmount() {
        return amount;
    }

    public PaylaterPlans setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PaylaterPlans setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public List<InstallmentPlan> getPlans() {
        return plans;
    }

    public PaylaterPlans setPlans(List<InstallmentPlan> plans) {
        this.plans = plans;
        return this;
    }

    public String getId() {
        return id;
    }

    public PaylaterPlans setId(String id) {
        this.id = id;
        return this;
    }

    public BaseTransaction.Status getStatus() {
        return status;
    }

    public PaylaterPlans setStatus(BaseTransaction.Status status) {
        this.status = status;
        return this;
    }

    public PaylaterPlans map(PaylaterPlans paylaterPlans, ApiInstallmentPlans jsonPlans) {
        if (paylaterPlans != null && jsonPlans != null) {
            paylaterPlans.setId(jsonPlans.getId())
                    .setAmount(jsonPlans.getAmount())
                    .setCurrency(jsonPlans.getCurrency());

            map(paylaterPlans.getPlans(), jsonPlans.getPlans());
        }
        return paylaterPlans;
    }

    private void map(List<InstallmentPlan> plans, List<ApiInstallmentPlan> jsonPlans) {
        if (jsonPlans == null) {
            return;
        }
        jsonPlans.forEach((plan) -> {
            List<InstallmentPlanRate> planRates = new ArrayList<>();
            mapRates(planRates, plan.getInstallmentRates());
            plans.add(new InstallmentPlan().setTotalAmount(plan.getTotalAmount())
                    .setNumberOfRates(plan.getNumberOfRates())
                    .setNominalInterestRate(plan.getNominalInterestRate())
                    .setEffectiveInterestRate(plan.getEffectiveInterestRate())
                    .setInstallmentRates(planRates)
                    .setSecciUrl(plan.getSecciUrl()));
        });
    }

    private void mapRates(List<InstallmentPlanRate> planRates, List<ApiInstallmentPlanRate> json) {
        if (json == null) {
            return;
        }
        json.forEach((rate) ->
                planRates.add(new InstallmentPlanRate().setRate(rate.getRate()).setDate(rate.getDate())));
    }
}
