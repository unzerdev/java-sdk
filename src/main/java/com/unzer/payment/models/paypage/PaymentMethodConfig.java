package com.unzer.payment.models.paypage;

import com.unzer.payment.models.WeroEventDependentPayment;
import lombok.Data;

@Data
public class PaymentMethodConfig {
    private Boolean enabled;
    private Integer order;

    // Type specific configs.
    private String label; // paylater types only.

    private Boolean credentialOnFile = null; // card only.
    private String exemption; // card only.

    private WeroEventDependentPayment eventDependentPayment; // wero only

    public PaymentMethodConfig(boolean enabled, Integer order) {
        this.enabled = enabled;
        this.order = order;
    }

    public PaymentMethodConfig(boolean enabled) {
        this.enabled = enabled;
    }
}
