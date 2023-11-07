package com.unzer.payment.marketplace;

import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Unzer;

import java.math.BigDecimal;

public class MarketplaceCancel extends BaseTransaction<MarketplacePayment> {

    private static final String FULL_AUTHORIZE_CANCEL_URL =
            "/v1/marketplace/payments/%1$s/authorize/cancels";
    private static final String FULL_CHARGES_CANCEL_URL =
            "/v1/marketplace/payments/%1$s/charges/cancels";
    private static final String PARTIAL_AUTHORIZE_CANCEL_URL =
            "/v1/marketplace/payments/%1$s/authorize/%2$s/cancels";
    private static final String PARTIAL_CHARGE_CANCEL_URL =
            "/v1/marketplace/payments/%1$s/charges/%2$s/cancels";

    private BigDecimal amountGross;
    private BigDecimal amountNet;
    private BigDecimal amountVat;
    private MarketplaceCancelBasket canceledBasket;

    public MarketplaceCancel() {
        super();
    }

    @Deprecated
    public MarketplaceCancel(Unzer unzer) {
        super(unzer);
    }

    public String getFullAuthorizeCancelUrl(String paymentId) {
        return String.format(FULL_AUTHORIZE_CANCEL_URL, paymentId);
    }

    public String getFullChargesCancelUrl(String paymentId) {
        return String.format(FULL_CHARGES_CANCEL_URL, paymentId);
    }

    public String getPartialAuthorizeCancelUrl(String paymentId, String authorizeId) {
        return String.format(PARTIAL_AUTHORIZE_CANCEL_URL, paymentId, authorizeId);
    }

    public String getPartialChargeCancelUrl(String paymentId, String chargeId) {
        return String.format(PARTIAL_CHARGE_CANCEL_URL, paymentId, chargeId);
    }

    @Override
    public String getTransactionUrl() {
        throw new UnsupportedOperationException(
                "Unsupported method. Please use other specific get URL methods.");
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

    public MarketplaceCancelBasket getCanceledBasket() {
        return canceledBasket;
    }

    public void setCanceledBasket(MarketplaceCancelBasket canceledBasket) {
        this.canceledBasket = canceledBasket;
    }
}
