/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unzer.payment.models.paylater;

import com.unzer.payment.models.CustomerType;
import java.math.BigDecimal;

public class InstallmentPlansRequest {
  private final BigDecimal amount;
  private final String currency;
  private final String country;
  private final CustomerType customerType;

  public InstallmentPlansRequest(BigDecimal amount, String currency, String country,
                                 CustomerType customerType) {
    this.amount = amount;
    this.currency = currency;
    this.country = country;
    this.customerType = customerType;
  }

  public String getRequestUrl() {
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
