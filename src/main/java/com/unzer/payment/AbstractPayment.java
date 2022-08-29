package com.unzer.payment;

import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;

public abstract class AbstractPayment implements PaymentType {
    protected static final String CANCEL_IS_ONLY_POSSIBLE_FOR_AN_AUTHORIZATION = "Cancel is only possible for an Authorization";
    protected static final String PAYMENT_CANCELLATION_NOT_POSSIBLE = "Payment cancellation not possible";
    private String id;
    private State paymentState;
    private BigDecimal amountTotal;
    private BigDecimal amountCharged;
    private BigDecimal amountCanceled;
    private BigDecimal amountRemaining;
    private String orderId;
    private String customerId;
    private Customer customer;
    private String paymentTypeId;
    private PaymentType paymentType;
    private String metadataId;
    private Metadata metadata;
    private String basketId;
    private Basket basket;
    private transient Unzer unzer;

    public AbstractPayment(Unzer unzer) {
        super();
        this.setUnzer(unzer);
    }

    public AbstractPayment() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Unzer getUnzer() {
        return unzer;
    }

    public void setUnzer(Unzer unzer) {
        this.unzer = unzer;
    }

    public PaymentType getPaymentType() throws HttpCommunicationException {
        if (paymentType == null) {
            paymentType = fetchPaymentType(getPaymentTypeId());
        }
        return paymentType;
    }

    public Customer getCustomer() throws HttpCommunicationException {
        if (customer == null && isNotEmpty(getCustomerId())) {
            customer = fetchCustomer(getCustomerId());
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public Metadata getMetadata() throws HttpCommunicationException {
        if (metadata == null && isNotEmpty(getMetadataId())) {
            metadata = fetchMetadata(getMetadataId());
        }
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public Basket getBasket() throws HttpCommunicationException {
        if (basket == null && isNotEmpty(getBasketId())) {
            basket = fetchBasket(getBasketId());
        }
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public State getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(State paymentState) {
        this.paymentState = paymentState;
    }

    public BigDecimal getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(BigDecimal amountTotal) {
        this.amountTotal = amountTotal;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
    }

    public BigDecimal getAmountCanceled() {
        return amountCanceled;
    }

    public void setAmountCanceled(BigDecimal amountCanceled) {
        this.amountCanceled = amountCanceled;
    }

    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(BigDecimal amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    protected boolean isNotEmpty(String value) {
        return value != null && !"".equalsIgnoreCase(value.trim());
    }

    protected PaymentType fetchPaymentType(String paymentTypeId) throws HttpCommunicationException {
        return getUnzer().fetchPaymentType(paymentTypeId);
    }

    protected Customer fetchCustomer(String customerId) throws HttpCommunicationException {
        return getUnzer().fetchCustomer(customerId);
    }

    protected Metadata fetchMetadata(String metadataId) throws HttpCommunicationException {
        return getUnzer().fetchMetadata(metadataId);
    }

    protected Basket fetchBasket(String basketId) throws HttpCommunicationException {
        return getUnzer().fetchBasket(basketId);
    }

    public enum State {
        COMPLETED, PENDING, CANCELED, PARTLY, PAYMENT_REVIEW, CHARGEBACK
    }
}
