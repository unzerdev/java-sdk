package com.unzer.payment;

import java.math.BigDecimal;

/**
 * Business object for Cancellations
 *
 * @author Unzer E-Com GmbH
 */
public class Cancel extends BaseTransaction<Payment> {

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
  public String getTransactionUrl() {
    // TODO: <authorizeId>: https://stg-api.unzer.com/api-reference/index.html#tag/Payments/operation/getCancelByAuthorize
    return "/v1/payments/<paymentId>/authorize/cancels/<transactionId>";
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
