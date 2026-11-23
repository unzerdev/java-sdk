package com.unzer.payment;

/**
 * @deprecated Use {@link PaylaterPlans} instead. Kept as a backward-compatible alias returned by
 * {@link Unzer#fetchPaylaterInstallmentPlans(com.unzer.payment.models.paylater.InstallmentPlansRequest)}.
 */
@Deprecated
public class PaylaterInstallmentPlans extends PaylaterPlans {

    public String getTypeUrl() {
        return "types/paylater-installment/plans";
    }
}
