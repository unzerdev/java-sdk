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

package com.unzer.payment.communication.json;

import java.math.BigDecimal;

/**
 * Apple Pay business object
 *
 * @author Unzer E-Com GmbH
 */
public class JsonApplepayResponse extends JsonIdObject implements JsonObject {
  private String applicationPrimaryAccountNumber;
  private String applicationExpirationDate;
  private String currencyCode;
  private BigDecimal transactionAmount;

  public String getApplicationPrimaryAccountNumber() {
    return applicationPrimaryAccountNumber;
  }

  public void setApplicationPrimaryAccountNumber(String applicationPrimaryAccountNumber) {
    this.applicationPrimaryAccountNumber = applicationPrimaryAccountNumber;
  }

  public String getApplicationExpirationDate() {
    return applicationExpirationDate;
  }

  public void setApplicationExpirationDate(String applicationExpirationDate) {
    this.applicationExpirationDate = applicationExpirationDate;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public BigDecimal getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(BigDecimal transactionAmount) {
    this.transactionAmount = transactionAmount;
  }
}
