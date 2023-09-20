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

import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.PaymentType;
import java.math.BigDecimal;

/**
 * Business object for Cancellations
 *
 * @author Unzer E-Com GmbH
 */
public class Cancel extends AbstractTransaction<Payment> {

  private BigDecimal amountGross;
  private BigDecimal amountNet;
  private BigDecimal amountVat;
  private ReasonCode reasonCode;

  public Cancel() {
    super();
  }

  @Deprecated
  public Cancel(Unzer unzer) {
    super(unzer);
  }

  @Override
  public String getTypeUrl() {
    return "payments/<paymentId>/authorize/cancels";
  }

  @Override
  public PaymentType map(PaymentType paymentType, ApiObject apiObject) {
    return null;
  }

  public BigDecimal getAmountGross() {
    return amountGross;
  }

  public void setAmountGross(BigDecimal amountGross) {
    this.amountGross = amountGross;
  }

  public BigDecimal getAmountNet() {
    return amountNet;
  }

  public void setAmountNet(BigDecimal amountNet) {
    this.amountNet = amountNet;
  }

  public BigDecimal getAmountVat() {
    return amountVat;
  }

  public void setAmountVat(BigDecimal amountVat) {
    this.amountVat = amountVat;
  }

  public ReasonCode getReasonCode() {
    return reasonCode;
  }

  public Cancel setReasonCode(ReasonCode reasonCode) {
    this.reasonCode = reasonCode;
    return this;
  }

  public enum ReasonCode {
    CANCEL, RETURN, CREDIT
  }
}
