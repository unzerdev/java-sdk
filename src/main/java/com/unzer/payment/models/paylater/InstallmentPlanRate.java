package com.unzer.payment.models.paylater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstallmentPlanRate {
    private Date date;
    private BigDecimal rate;
}
