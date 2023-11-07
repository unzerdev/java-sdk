package com.unzer.payment.models;

import java.util.Locale;

public class PaylaterInvoiceConfigRequest {
    private final CustomerType customerType;
    private final Locale country;


    /**
     * @param customerType mandatory field
     * @param country      ISO-2 format
     */
    public PaylaterInvoiceConfigRequest(CustomerType customerType, Locale country) {
        this.customerType = customerType;
        this.country = country;
    }

    public String getRequestUrl() {
        StringBuilder base = new StringBuilder("/v1/types/paylater-invoice/config?customerType=");
        base.append(customerType);

        if (country != null) {
            base.append("&country=");
            base.append(country.getCountry());
        }

        return base.toString();
    }
}
