package com.unzer.payment.models.paylater;

import com.unzer.payment.models.CustomerType;

import java.math.BigDecimal;

/**
 * @deprecated Use {@link PaylaterPlansRequest} together with
 * {@link PaylaterPlanType#INSTALLMENT} instead. Kept for backward compatibility; it defaults
 * to the installment plans endpoint.
 */
@Deprecated
public class InstallmentPlansRequest extends PaylaterPlansRequest {
    public InstallmentPlansRequest(BigDecimal amount, String currency, String country,
                                   CustomerType customerType) {
        super(amount, currency, country, customerType);
    }

    @Override
    public String getUrl() {
        return getUrl(PaylaterPlanType.INSTALLMENT);
    }
}
