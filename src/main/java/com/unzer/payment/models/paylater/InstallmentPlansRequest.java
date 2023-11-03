package com.unzer.payment.models.paylater;

import com.unzer.payment.Resource;
import com.unzer.payment.models.CustomerType;
import java.math.BigDecimal;

public class InstallmentPlansRequest implements Resource {
  private final BigDecimal amount;
  private final String currency;
  private final String country;
  private final CustomerType customerType;

  public InstallmentPlansRequest(
      BigDecimal amount,
      String currency,
      String country,
      CustomerType customerType
  ) {
    this.amount = amount;
    this.currency = currency;
    this.country = country;
    this.customerType = customerType;
  }

  @Override
  public String getId() {
    throw new UnsupportedOperationException("InstallmentPlansRequest has no id");
  }

  @Override
  public String getUrl() {
    StringBuilder base = new StringBuilder("/v1/types/paylater-installment/plans?amount=");
    base.append(amount.toString());

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
