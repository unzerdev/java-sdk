package com.unzer.payment.models.paypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AmountSettings {
    private BigDecimal minimum;
    private BigDecimal maximum;
}
