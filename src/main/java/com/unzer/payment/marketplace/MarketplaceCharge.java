package com.unzer.payment.marketplace;

import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceCharge extends BaseTransaction<MarketplacePayment> {

  private static final String MARKETPLACE_AUTHORIZATION_CHARGES =
      "/v1/marketplace/payments/%1$s/authorize/%2$s/charges";

  private static final String MARKETPLACE_FULL_AUTHORIZATIONS_CHARGES =
      "/v1/marketplace/payments/%1$s/authorize/charges";

  private static final String MARKETPLACE_DIRECT_CHARGES =
      "/v1/marketplace/payments/<paymentId>/charges";

  private String invoiceId;

  private List<MarketplaceCancel> cancelList;

  public MarketplaceCharge() {
    super();
    setCancelList(new ArrayList<MarketplaceCancel>());
  }

  @Deprecated
  public MarketplaceCharge(Unzer unzer) {
    super(unzer);
    setCancelList(new ArrayList<MarketplaceCancel>());
  }

  @Override
  public String getTransactionUrl() {
    return MARKETPLACE_DIRECT_CHARGES;
  }

  public String getFullChargeAuthorizationsUrl(String paymentId) {
    return String.format(MARKETPLACE_FULL_AUTHORIZATIONS_CHARGES, paymentId);
  }

  public String getChargeAuthorizationUrl(String paymentId, String authorizeId) {
    return String.format(MARKETPLACE_AUTHORIZATION_CHARGES, paymentId, authorizeId);
  }

  public String getInvoiceId() {
    return invoiceId;
  }

  public MarketplaceCharge setInvoiceId(String invoiceId) {
    this.invoiceId = invoiceId;
    return this;
  }

  public List<MarketplaceCancel> getCancelList() {
    return cancelList;
  }

  public void setCancelList(List<MarketplaceCancel> cancelList) {
    this.cancelList = cancelList;
  }

  /**
   * Cancel for this charge.
   *
   * @param cancel refers to MarketplaceCancel.
   * @return MarketplaceCancel
   * @throws HttpCommunicationException generic Payment API communication error
   * @deprecated use {@link Unzer#marketplaceChargeCancel(String, String, MarketplaceCancel)}
   */
  @Deprecated
  public MarketplaceCancel cancel(MarketplaceCancel cancel) throws HttpCommunicationException {
    return getUnzer().marketplaceChargeCancel(this.getPaymentId(), this.getId(), cancel);
  }
}
