package com.unzer.payment;

import com.unzer.payment.communication.json.paylater.ApiInstallmentPlans;
import com.unzer.payment.models.paylater.InstallmentPlan;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/**
 * @deprecated Use {@link PaylaterPlans} instead. Kept as a backward-compatible alias returned by
 * {@link Unzer#fetchPaylaterInstallmentPlans(com.unzer.payment.models.paylater.InstallmentPlansRequest)}.
 * The covariant setter overrides and the {@code map} overload are retained to preserve source and
 * binary compatibility for existing callers.
 */
@Deprecated
public class PaylaterInstallmentPlans extends PaylaterPlans {

    public String getTypeUrl() {
        return "types/paylater-installment/plans";
    }

    @Override
    public PaylaterInstallmentPlans setAmount(BigDecimal amount) {
        super.setAmount(amount);
        return this;
    }

    @Override
    public PaylaterInstallmentPlans setCurrency(Currency currency) {
        super.setCurrency(currency);
        return this;
    }

    @Override
    public PaylaterInstallmentPlans setPlans(List<InstallmentPlan> plans) {
        super.setPlans(plans);
        return this;
    }

    @Override
    public PaylaterInstallmentPlans setId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public PaylaterInstallmentPlans setStatus(BaseTransaction.Status status) {
        super.setStatus(status);
        return this;
    }

    public PaylaterInstallmentPlans map(PaylaterInstallmentPlans installmentPlans,
                                        ApiInstallmentPlans jsonInstallmentPlans) {
        super.map(installmentPlans, jsonInstallmentPlans);
        return installmentPlans;
    }
}
