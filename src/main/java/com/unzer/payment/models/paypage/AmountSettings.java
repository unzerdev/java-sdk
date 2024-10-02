package com.unzer.payment.models.paypage;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class AmountSettings {
    private BigDecimal minimum;
    private BigDecimal maximum;
}
