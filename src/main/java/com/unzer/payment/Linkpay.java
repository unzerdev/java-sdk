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

package com.unzer.payment;

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Currency;
import java.util.Map;

public class Linkpay extends BasePaypage {
  private String version;

  private String alias;

  private String expires;

  private String intention;

  private String paymentReference;

  private String orderIdRequired;

  private String invoiceIdRequired;

  private String oneTimeUse;

  private String successfullyProcessed;

  @Override
  public String getTypeUrl() {
    return "linkpay";
  }


  @Override
  public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
    return null;
  }

  /**
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @param alias the alias to set
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * @param version the version to set
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * @return the expires
   */
  public String getExpires() {
    return expires;
  }

  /**
   * @param expires the expires to set
   */
  public void setExpires(String expires) {
    this.expires = expires;
  }


  /**
   * @return the intention
   */
  public String getIntention() {
    return intention;
  }

  /**
   * @param intention the intention to set
   */
  public void setIntention(String intention) {
    this.intention = intention;
  }

  public String getOrderIdRequired() {
    return orderIdRequired;
  }

  public void setOrderIdRequired(String orderIdRequired) {
    this.orderIdRequired = orderIdRequired;
  }

  public String getInvoiceIdRequired() {
    return invoiceIdRequired;
  }

  public void setInvoiceIdRequired(String invoiceIdRequired) {
    this.invoiceIdRequired = invoiceIdRequired;
  }

  public String getOneTimeUse() {
    return oneTimeUse;
  }

  public void setOneTimeUse(String oneTimeUse) {
    this.oneTimeUse = oneTimeUse;
  }

  public String getSuccessfullyProcessed() {
    return successfullyProcessed;
  }

  public void setSuccessfullyProcessed(String successfullyProcessed) {
    this.successfullyProcessed = successfullyProcessed;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
  }
}
