package com.unzer.payment;

import com.unzer.payment.communication.HttpCommunicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Business object for Charge. Amount, currency and typeId are mandatory parameter to
 * execute a Charge.
 * The returnUrl is mandatory in case of redirectPayments like Sofort, Paypal, Giropay, Card 3DS.
 *
 * @author Unzer E-Com GmbH
 */
public class Charge extends BaseTransaction<Payment> {
  private List<Cancel> cancelList;

  public Charge() {
    super();
    setCancelList(new ArrayList<Cancel>());
  }

  @Deprecated
  public Charge(Unzer unzer) {
    super(unzer);
    setCancelList(new ArrayList<Cancel>());
  }

  @Override
  protected String getTransactionUrl() {
    return "/v1/payments/<paymentId>/charges/<transactionId>";
  }

  /**
   * @deprecated use {@link Unzer#cancelCharge(String, String)} instead
   */
  @Deprecated
  public Cancel cancel() throws HttpCommunicationException {
    return getUnzer().cancelCharge(getPayment().getId(), getId());
  }

  /**
   * @deprecated use {@link Unzer#cancelCharge(String, String, Cancel)} instead
   */
  @Deprecated
  public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
    return getUnzer().cancelCharge(getPayment().getId(), getId(), amount);
  }

  /**
   * @deprecated use {@link Unzer#cancelCharge(String, String, Cancel)} instead
   */
  @Deprecated
  public Cancel cancel(Cancel cancel) throws HttpCommunicationException {
    return getUnzer().cancelCharge(getPayment().getId(), getId(), cancel);
  }

  public List<Cancel> getCancelList() {
    return cancelList;
  }

  public void setCancelList(List<Cancel> cancelList) {
    this.cancelList = cancelList;
  }

  public Cancel getCancel(String cancelId) {
    if (cancelList == null) {
      return null;
    }
    for (Cancel cancel : cancelList) {
      if (cancelId.equalsIgnoreCase(cancel.getId())) {
        return cancel;
      }
    }
    return null;
  }
}
