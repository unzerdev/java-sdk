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

import com.unzer.payment.marketplace.MarketplaceCancelBasket;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public class JsonCancel extends JsonIdObject implements JsonObject, TransactionStatus {
  private Boolean isSuccess;
  private Boolean isPending;
  private Boolean isError;

  private JsonMessage message;
  private Date date;
  private String paymentReference;
  private String reasonCode;

  private String orderId;
  private String invoiceId;

  private BigDecimal amount;
  private Currency currency;
  private URL returnUrl;

  private JsonResources resources;
  private JsonProcessing processing = new JsonProcessing();
  private MarketplaceCancelBasket canceledBasket;

  public BigDecimal getAmount() {
    return amount;
  }

  public JsonCancel setAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public Currency getCurrency() {
    return currency;
  }

  public JsonCancel setCurrency(Currency currency) {
    this.currency = currency;
    return this;
  }

  public URL getReturnUrl() {
    return returnUrl;
  }

  public JsonCancel setReturnUrl(URL returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }

  public JsonProcessing getProcessing() {
    return processing;
  }

  public JsonCancel setProcessing(JsonProcessing processing) {
    this.processing = processing;
    return this;
  }


  public JsonResources getResources() {
    return resources;
  }

  public void setResources(JsonResources resources) {
    this.resources = resources;
  }

  public JsonMessage getMessage() {
    return message;
  }

  public void setMessage(JsonMessage message) {
    this.message = message;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Boolean getSuccess() {
    return isSuccess;
  }

  public void setSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public Boolean getPending() {
    return isPending;
  }

  public void setPending(Boolean isPending) {
    this.isPending = isPending;
  }

  @Override
  public Boolean getResumed() {
    return null;
  }

  @Override
  public void setResumed(Boolean value) {
    // Do nothing
  }

  public Boolean getError() {
    return isError;
  }

  public void setError(Boolean isError) {
    this.isError = isError;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
  }

  public MarketplaceCancelBasket getCanceledBasket() {
    return canceledBasket;
  }

  public void setCanceledBasket(MarketplaceCancelBasket canceledBasket) {
    this.canceledBasket = canceledBasket;
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public void setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }
}
