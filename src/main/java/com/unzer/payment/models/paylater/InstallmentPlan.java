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
}
