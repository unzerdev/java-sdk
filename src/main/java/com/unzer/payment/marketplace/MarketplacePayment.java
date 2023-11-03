package com.unzer.payment.marketplace;

import com.unzer.payment.BasePayment;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import java.util.List;

public class MarketplacePayment extends BasePayment {

  private List<MarketplaceAuthorization> authorizationsList;
  private List<MarketplaceCharge> chargesList;
  private List<MarketplaceCancel> cancelList;

  public MarketplacePayment() {
    super();
  }

  @Deprecated
  public MarketplacePayment(Unzer unzer) {
    super(unzer);
  }

  public MarketplaceCharge getCharge(String chargeId) {
    if (chargesList == null) {
      return null;
    }
    for (MarketplaceCharge charge : chargesList) {
      if (chargeId.equalsIgnoreCase(charge.getId())) {
        return charge;
      }
    }
    return null;
  }

  public MarketplaceCharge getCharge(int index) {
    return getChargesList().get(index);
  }

  public List<MarketplaceCharge> getChargesList() {
    return chargesList;
  }

  public void setChargesList(List<MarketplaceCharge> chargesList) {
    this.chargesList = chargesList;
  }

  public MarketplaceCancel getCancel(String cancelId) {
    if (getCancelList() == null) {
      return null;
    }
    for (MarketplaceCancel cancel : getCancelList()) {
      if (cancelId.equalsIgnoreCase(cancel.getId())) {
        return cancel;
      }
    }
    return null;
  }

  public List<MarketplaceCancel> getCancelList() {
    return cancelList;
  }

  public void setCancelList(List<MarketplaceCancel> cancelList) {
    this.cancelList = cancelList;
  }

  public MarketplaceAuthorization getAuthorization(String authorizeId) {
    if (authorizationsList == null) {
      return null;
    }
    for (MarketplaceAuthorization charge : authorizationsList) {
      if (authorizeId.equalsIgnoreCase(charge.getId())) {
        return charge;
      }
    }
    return null;
  }

  public MarketplaceAuthorization getAuthorization(int index) {
    return getAuthorizationsList().get(index);
  }

  public List<MarketplaceAuthorization> getAuthorizationsList() {
    return authorizationsList;
  }

  public void setAuthorizationsList(List<MarketplaceAuthorization> authorizationsList) {
    this.authorizationsList = authorizationsList;
  }

  /**
   * Fully cancel for Marketplace Authorization(s).
   * <b>Note:</b>: <code>amount</code> will be ignored due to fully cancel.
   * Only <code>paymentReference</code> is processed.
   *
   * @return MarketplacePayment
   * @throws HttpCommunicationException generic Payment API communication error
   * @deprecated use {@link Unzer#marketplaceFullAuthorizationsCancel(String, MarketplaceCancel)}
   */
  @Deprecated
  public MarketplacePayment marketplaceFullAuthorizeCancel(String paymentReference)
      throws HttpCommunicationException {
    MarketplaceCancel cancel = new MarketplaceCancel();
    cancel.setPaymentReference(paymentReference);
    return getUnzer().marketplaceFullAuthorizationsCancel(this.getId(), cancel);
  }

  /**
   * Fully cancel for Marketplace Charge(s).
   * <b>Note:</b>: <code>amount</code> will be ignored due to fully cancel.
   * Only <code>paymentReference</code> is processed.
   *
   * @return MarketplacePayment
   * @throws HttpCommunicationException generic Payment API communication error
   * @deprecated use {@link Unzer#marketplaceFullChargesCancel(String, MarketplaceCancel)} instead
   */
  @Deprecated
  public MarketplacePayment marketplaceFullChargesCancel(String paymentReference)
      throws HttpCommunicationException {
    MarketplaceCancel cancel = new MarketplaceCancel();
    cancel.setPaymentReference(paymentReference);
    return getUnzer().marketplaceFullChargesCancel(this.getId(), cancel);
  }

  /**
   * Fully charge for Marketplace Authorization(s).
   * <b>Note:</b>: <code>amount</code> will be ignored due to fully charge.
   * Only <code>paymentReference</code> is processed.
   *
   * @return MarketplacePayment
   * @throws HttpCommunicationException generic Payment API communication error
   * @deprecated use {@link Unzer#marketplaceFullChargeAuthorizations(String, String)} instead
   */
  @Deprecated
  public MarketplacePayment fullChargeAuthorizations(String paymentReference)
      throws HttpCommunicationException {
    return getUnzer().marketplaceFullChargeAuthorizations(this.getId(), paymentReference);
  }

  @Override
  protected String getResourceUrl() {
    return "/v1/marketplace/payments/<resourceId>";
  }
}
