package com.unzer.payment.marketplace;

import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.paymenttypes.PaymentType;
import java.util.List;

public class MarketplaceAuthorization extends BaseTransaction<MarketplacePayment> {

  private List<MarketplaceCancel> cancelList;

  public MarketplaceAuthorization() {
    super();
  }

  @Deprecated
  public MarketplaceAuthorization(Unzer unzer) {
    super(unzer);
  }

  @Override
  public String getTransactionUrl() {
    return "/v1/marketplace/payments/<paymentId>/authorize/<transactionId>";
  }

  public List<MarketplaceCancel> getCancelList() {
    return cancelList;
  }

  public void setCancelList(List<MarketplaceCancel> cancelList) {
    this.cancelList = cancelList;
  }

  public MarketplaceCancel getCancel(String cancelId) {
    if (cancelList == null) {
      return null;
    }
    for (MarketplaceCancel cancel : cancelList) {
      if (cancelId.equalsIgnoreCase(cancel.getId())) {
        return cancel;
      }
    }
    return null;
  }

  /**
   * @deprecated use {@link Unzer#marketplaceCharge(MarketplaceCharge)} instead
   */
  @Deprecated
  public MarketplaceCharge charge(MarketplaceCharge charge) throws HttpCommunicationException {
    return getUnzer().marketplaceChargeAuthorization(this.getPaymentId(), this.getId(), charge);
  }

  /**
   * Cancel for this authorization.
   *
   * @param cancel refers to MarketplaceCancel.
   * @return MarketplaceCancel
   * @throws HttpCommunicationException generic Payment API communication error
   * @deprecated use {@link Unzer#marketplaceAuthorizationCancel(String, String, MarketplaceCancel)}
   */
  @Deprecated
  public MarketplaceCancel cancel(MarketplaceCancel cancel) throws HttpCommunicationException {
    return getUnzer().marketplaceAuthorizationCancel(this.getPaymentId(), this.getId(), cancel);
  }
}
