package com.unzer.payment.marketplace;

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.util.List;

public class MarketplaceAuthorization extends AbstractTransaction<MarketplacePayment> {

    private List<MarketplaceCancel> cancelList;

    public MarketplaceAuthorization() {
        super();
    }

    public MarketplaceAuthorization(Unzer unzer) {
        super(unzer);
    }

    @Override
    public String getTypeUrl() {
        return "marketplace/payments/<paymentId>/authorize";
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    public List<MarketplaceCancel> getCancelList() {
        return cancelList;
    }

    public void setCancelList(List<MarketplaceCancel> cancelList) {
        this.cancelList = cancelList;
    }

    public MarketplaceCancel getCancel(String cancelId) {
        if (cancelList == null) return null;
        for (MarketplaceCancel cancel : cancelList) {
            if (cancelId.equalsIgnoreCase(cancel.getId())) {
                return cancel;
            }
        }
        return null;
    }

    public MarketplaceCharge charge(MarketplaceCharge charge) throws HttpCommunicationException {
        return getUnzer().marketplaceChargeAuthorization(this.getPaymentId(), this.getId(), charge);
    }

    /**
     * Cancel for this authorization.
     *
     * @param cancel refers to MarketplaceCancel.
     * @return MarketplaceCancel
     * @throws HttpCommunicationException
     */
    public MarketplaceCancel cancel(MarketplaceCancel cancel) throws HttpCommunicationException {
        return getUnzer().marketplaceAuthorizationCancel(this.getPaymentId(), this.getId(), cancel);
    }
}
