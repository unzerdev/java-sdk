package com.unzer.payment;

import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.BasePaymentType;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;

/**
 * Business object for a Payment. A Payment is the object that holds toghether several
 * requests over time. This means that a payment belongs to one offer from the merchant.
 * <p>
 * Within the Payment you also find the list of Charges, Cancels and the Authorization object.
 *
 * @author Unzer E-Com GmbH
 */
public class Payment extends BasePayment {

    private Authorization authorization;
    private List<Charge> chargesList;
    private List<Cancel> cancelList;
    private List<Payout> payoutList;
    private List<Chargeback> chargebackList;

    public Payment() {
        super();
    }

    @Deprecated
    public Payment(Unzer unzer) {
        super(unzer);
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge() throws HttpCommunicationException {
        return getUnzer().chargeAuthorization(getId());
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount) throws HttpCommunicationException {
        return getUnzer().chargeAuthorization(getId(), amount);
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, String typeId)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId);
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId, customerId);
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, BasePaymentType paymentType)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, paymentType);
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl)
            throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId, returnUrl);
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         String customerId) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId, returnUrl, customerId);
    }

    /**
     * @deprecated use {@link Unzer#charge(Charge)} instead
     */
    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, BasePaymentType paymentType,
                         URL returnUrl,
                         Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, paymentType, returnUrl, customer);
    }

    /**
     * @deprecated use {@link Unzer#authorize(Authorization)} instead
     */
    @Deprecated
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl)
            throws HttpCommunicationException {
        return getUnzer().authorize(amount, currency, typeId, returnUrl);
    }

    /**
     * @deprecated use {@link Unzer#authorize(Authorization)} instead
     */
    @Deprecated
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                                   String customerId) throws HttpCommunicationException {
        return getUnzer().authorize(amount, currency, typeId, returnUrl, customerId);
    }

    /**
     * @deprecated use {@link Unzer#authorize(Authorization)} instead
     */
    @Deprecated
    public Authorization authorize(BigDecimal amount, Currency currency, BasePaymentType paymentType,
                                   URL returnUrl, Customer customer)
            throws HttpCommunicationException {
        return getUnzer().authorize(amount, currency, paymentType, returnUrl, customer);
    }

    /**
     * @deprecated use {@link Unzer#cancelAuthorization(String, Cancel)} or
     * {@link Unzer#cancelCharge(String, String)} instead.
     */
    @Deprecated
    public Cancel cancel() throws HttpCommunicationException {
        if (getAuthorization() == null) {
            List<PaymentError> paymentErrorList = new ArrayList<PaymentError>();
            paymentErrorList.add(new PaymentError(CANCEL_IS_ONLY_POSSIBLE_FOR_AN_AUTHORIZATION,
                    PAYMENT_CANCELLATION_NOT_POSSIBLE, ""));

            throw new PaymentException(paymentErrorList, "");
        }
        return getAuthorization().cancel();
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    /**
     * @deprecated use {@link Unzer#cancelAuthorization(String, Cancel)}
     * or {@link Unzer#cancelCharge(String, String)} instead.
     */
    @Deprecated
    public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
        if (getAuthorization() == null) {
            List<PaymentError> paymentErrorList = new ArrayList<>();
            paymentErrorList.add(new PaymentError(CANCEL_IS_ONLY_POSSIBLE_FOR_AN_AUTHORIZATION,
                    PAYMENT_CANCELLATION_NOT_POSSIBLE, ""));

            throw new PaymentException(paymentErrorList, "");
        }
        return getAuthorization().cancel(amount);
    }

    public Charge getCharge(String chargeId) {
        return findById(chargesList, chargeId);
    }

    private <T extends BaseTransaction<Payment>> T findById(Collection<T> collection, String id) {
        if (collection == null) {
            return null;
        }

        return collection.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .findAny()
                .orElse(null);
    }

    public Charge getCharge(int index) {
        return getChargesList().get(index);
    }

    public List<Charge> getChargesList() {
        return chargesList;
    }

    public void setChargesList(List<Charge> chargesList) {
        this.chargesList = chargesList;
    }

    public Payout getPayout(String payoutId) {
        return findById(payoutList, payoutId);
    }

    public List<Cancel> getCancelList() {
        return cancelList;
    }

    public void setCancelList(List<Cancel> cancelList) {
        this.cancelList = cancelList;
    }

    public Cancel getCancel(String cancelId) {
        return findById(cancelList, cancelId);
    }

    public Cancel getCancel() {
        return new Cancel();
    }

    public List<Payout> getPayoutList() {
        return payoutList;
    }

    public void setPayoutList(List<Payout> payoutList) {
        this.payoutList = payoutList;
    }

    public List<Chargeback> getChargebackList() {
        return chargebackList;
    }

    public void setChargebackList(List<Chargeback> chargebackList) {
        this.chargebackList = chargebackList;
    }

    @Override
    public String getResourceUrl() {
        return "/v1/payments/<resourceId>";
    }
}
