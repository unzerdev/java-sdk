package com.unzer.payment.models.paylater;

import com.unzer.payment.Resource;
import com.unzer.payment.models.CustomerType;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        throw new UnsupportedOperationException(
                "PaylaterPlansRequest has no implied plan type; use getUrl(PaylaterPlanType) "
                        + "or Unzer#fetchPaylaterPlans(PaylaterPlansRequest, PaylaterPlanType)");
    }

    public String getUrl(PaylaterPlanType type) {
        Objects.requireNonNull(type, "PaylaterPlanType must not be null");

        StringBuilder base = new StringBuilder("/v1/types/")
                .append(type.getSegment())
                .append("/plans?amount=")
                .append(encode(String.valueOf(amount)));

        if (currency != null) {
            base.append("&currency=");
            base.append(encode(currency));
        }
        if (country != null) {
            base.append("&country=");
            base.append(encode(country));
        }
        if (customerType != null) {
            base.append("&customerType=");
            base.append(encode(customerType.toString()));
        }

        return base.toString();
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
