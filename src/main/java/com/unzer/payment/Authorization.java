package com.unzer.payment;

import com.unzer.payment.communication.HttpCommunicationException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Business object for Authorization. Amount, currency and typeId are mandatory parameter to
 * execute an Authorization.
 * <p>
 * The returnUrl is mandatory in case of redirectPayments like Sofort, Paypal, Giropay, Card 3DS
 *
 * @author Unzer E-Com GmbH
 */
public class Authorization extends BaseTransaction<Payment> {

    private BigDecimal effectiveInterestRate;

    private List<Cancel> cancelList;

    public Authorization() {
        super();
    }

    @Deprecated
    public Authorization(Unzer unzer) {
        super(unzer);
    }

    @Override
    protected String getTransactionUrl() {
        return "/v1/payments/<paymentId>/authorize/<transactionId>";
    }

    /**
     * @deprecated use {@link Unzer#chargeAuthorization(Charge)} instead
     */
    @Deprecated
    public Charge charge() throws HttpCommunicationException {
        return getUnzer().chargeAuthorization(getPayment().getId());
    }

    /**
     * @deprecated use {@link Unzer#chargeAuthorization(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount) throws HttpCommunicationException {
        return getUnzer().chargeAuthorization(getPayment().getId(), amount);
    }

    /**
     * @deprecated use {@link Unzer#chargeAuthorization(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount, String paymentReference)
            throws HttpCommunicationException {
        return getUnzer().chargeAuthorization(getPayment().getId(), amount, paymentReference);
    }

    /**
     * @deprecated use {@link Unzer#cancelAuthorization(String, Cancel)} instead
     */
    @Deprecated
    public Cancel cancel() throws HttpCommunicationException {
        return getUnzer().cancelAuthorization(getPayment().getId());
    }

    /**
     * @deprecated use {@link Unzer#cancelAuthorization(String, Cancel)} instead
     */
    @Deprecated
    public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
        return getUnzer().cancelAuthorization(getPayment().getId(), amount);
    }

    /**
     * @deprecated use {@link Unzer#cancelAuthorization(String, Cancel)} instead
     */
    @Deprecated
    public Cancel cancel(Cancel cancel) throws HttpCommunicationException {
        return getUnzer().cancelAuthorization(getPayment().getId(), cancel);
    }

    public BigDecimal getEffectiveInterestRate() {
        return effectiveInterestRate;
    }

    public Authorization setEffectiveInterestRate(BigDecimal effectiveInterestRate) {
        this.effectiveInterestRate = effectiveInterestRate;
        return this;
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
