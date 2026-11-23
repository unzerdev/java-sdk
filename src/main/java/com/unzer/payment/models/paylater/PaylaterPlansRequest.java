package com.unzer.payment.models.paylater;

import com.unzer.payment.Resource;
import com.unzer.payment.models.CustomerType;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Request DTO for fetching Paylater plans (installment, invoice or direct debit).
 * This is a pass-through DTO: input is not validated by the SDK, invalid values are
 * rejected by the Payment API.
 */
public class PaylaterPlansRequest implements Resource {
    private final BigDecimal amount;
    private final String currency;
    private final String country;
    private final CustomerType customerType;

    public PaylaterPlansRequest(BigDecimal amount, String currency, String country,
                                CustomerType customerType) {
        this.amount = amount;
        this.currency = currency;
        this.country = country;
        this.customerType = customerType;
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("PaylaterPlansRequest has no id");
    }

    @Override
    public String getUrl() {
        return getUrl(PaylaterPlanType.INSTALLMENT);
    }

    public String getUrl(PaylaterPlanType type) {
        Objects.requireNonNull(type, "PaylaterPlanType must not be null");

        StringBuilder base = new StringBuilder("/v1/types/")
                .append(type.getSegment())
                .append("/plans?amount=")
                .append(amount.toString());

        if (currency != null) {
            base.append("&currency=");
            base.append(currency);
        }
        if (country != null) {
            base.append("&country=");
            base.append(country);
        }
        if (customerType != null) {
            base.append("&customerType=");
            base.append(customerType);
        }

        return base.toString();
    }
}
