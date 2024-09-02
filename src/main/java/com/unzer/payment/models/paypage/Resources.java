package com.unzer.payment.models.paypage;

import lombok.Data;

@Data
public class Resources {
    private String customerId;
    private String basketId;
    private String metadataId;
    private String[] paymentIds;

    public Resources(String customerId, String basketId, String metadataId) {
        this.customerId = customerId;
        this.basketId = basketId;
        this.metadataId = metadataId;
    }
}
