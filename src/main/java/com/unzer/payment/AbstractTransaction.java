/*
 * Unzer Java SDK
 *
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.unzer.payment;

import com.unzer.payment.communication.JsonFieldIgnore;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Date;

public abstract class AbstractTransaction<T extends AbstractPayment> implements PaymentType {

    private String id;
    private BigDecimal amount;
    private Currency currency;
    private URL returnUrl;
    private Boolean card3ds;
    private String orderId;
    private String typeId;
    private String customerId;
    private String metadataId;
    private String paymentId;
    private String riskId;
    private String basketId;
    private String paymentReference;
    private Status status;
    private URL redirectUrl;
    private Processing processing = new Processing();
    private String traceId;
    private Message message;
    private Date date;
    private String type;
    private AdditionalTransactionData additionalTransactionData;
    @JsonFieldIgnore
    private T payment;
    @JsonFieldIgnore
    private Unzer unzer;
    @JsonFieldIgnore
    private URL resourceUrl;

    public AbstractTransaction() {
        super();
    }

    public AbstractTransaction(Unzer unzer) {
        this.unzer = unzer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AbstractTransaction<T> setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AbstractTransaction<T> setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public URL getReturnUrl() {
        return returnUrl;
    }

    public AbstractTransaction<T> setReturnUrl(URL returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public Boolean getCard3ds() {
        return card3ds;
    }

    public AbstractTransaction<T> setCard3ds(Boolean card3ds) {
        this.card3ds = card3ds;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public AbstractTransaction<T> setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getTypeId() {
        return typeId;
    }

    public AbstractTransaction<T> setTypeId(String typeId) {
        this.typeId = typeId;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public AbstractTransaction<T> setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public AbstractTransaction<T> setMetadataId(String metadataId) {
        this.metadataId = metadataId;
        return this;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public AbstractTransaction<T> setPaymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public String getRiskId() {
        return riskId;
    }

    public AbstractTransaction<T> setRiskId(String riskId) {
        this.riskId = riskId;
        return this;
    }

    public String getBasketId() {
        return basketId;
    }

    public AbstractTransaction<T> setBasketId(String basketId) {
        this.basketId = basketId;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public AbstractTransaction<T> setStatus(Status status) {
        this.status = status;
        return this;
    }

    public AdditionalTransactionData getAdditionalTransactionData() {
        return additionalTransactionData;
    }

    public void setAdditionalTransactionData(AdditionalTransactionData additionalTransactionData) {
        this.additionalTransactionData = additionalTransactionData;
    }

    public URL getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(URL redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Processing getProcessing() {
        return processing;
    }

    public AbstractTransaction<T> setProcessing(Processing processing) {
        this.processing = processing;
        return this;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public T getPayment() {
        return payment;
    }

    public void setPayment(T payment) {
        this.payment = payment;
    }

    public Unzer getUnzer() {
        return unzer;
    }

    public void setUnzer(Unzer unzer) {
        this.unzer = unzer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URL getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(URL resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public enum Status {
        SUCCESS, PENDING, ERROR
    }
}
