package com.unzer.payment.models.paylater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentPlan {
    private BigDecimal totalAmount;
    private int numberOfRates;
    private BigDecimal nominalInterestRate;
    private BigDecimal effectiveInterestRate;
    private List<InstallmentPlanRate> installmentRates;
    private URL secciUrl;
    private BigDecimal minimumInstallmentFee;

    /**
     * Backward-compatible constructor preserving the original signature from before
     * {@code minimumInstallmentFee} was added (leaves that field {@code null}).
     */
    public InstallmentPlan(BigDecimal totalAmount, int numberOfRates, BigDecimal nominalInterestRate,
                           BigDecimal effectiveInterestRate, List<InstallmentPlanRate> installmentRates,
                           URL secciUrl) {
        this.totalAmount = totalAmount;
        this.numberOfRates = numberOfRates;
        this.nominalInterestRate = nominalInterestRate;
        this.effectiveInterestRate = effectiveInterestRate;
        this.installmentRates = installmentRates;
        this.secciUrl = secciUrl;
    }
}
