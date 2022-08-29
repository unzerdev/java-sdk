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

import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

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
public class Payment extends AbstractPayment {

    private Authorization authorization;
    private List<Charge> chargesList;
    private List<Cancel> cancelList;
    private List<Payout> payoutList;

    public Payment() {
        super();
    }

    public Payment(Unzer unzer) {
        super(unzer);
    }

    public Charge charge() throws HttpCommunicationException {
        return getUnzer().chargeAuthorization(getId());
    }

    public Charge charge(BigDecimal amount) throws HttpCommunicationException {
        return getUnzer().chargeAuthorization(getId(), amount);
    }

    public Charge charge(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId);
    }

    public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId, customerId);
    }

    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, paymentType);
    }

    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId, returnUrl);
    }

    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, typeId, returnUrl, customerId);
    }

    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, paymentType, returnUrl, customer);
    }

    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
        return getUnzer().authorize(amount, currency, typeId, returnUrl);
    }

    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
        return getUnzer().authorize(amount, currency, typeId, returnUrl, customerId);
    }

    public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().authorize(amount, currency, paymentType, returnUrl, customer);
    }

    public Cancel cancel() throws HttpCommunicationException {
        if (getAuthorization() == null) {
            List<PaymentError> paymentErrorList = new ArrayList<PaymentError>();
            paymentErrorList.add(new PaymentError(CANCEL_IS_ONLY_POSSIBLE_FOR_AN_AUTHORIZATION, PAYMENT_CANCELLATION_NOT_POSSIBLE, ""));

            throw new PaymentException(paymentErrorList, "");
        }
        return getAuthorization().cancel();
    }

    public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
        if (getAuthorization() == null) {
            List<PaymentError> paymentErrorList = new ArrayList<>();
            paymentErrorList.add(new PaymentError(CANCEL_IS_ONLY_POSSIBLE_FOR_AN_AUTHORIZATION, PAYMENT_CANCELLATION_NOT_POSSIBLE, ""));

            throw new PaymentException(paymentErrorList, "");
        }
        return getAuthorization().cancel(amount);
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public Charge getCharge(String chargeId) {
        return findById(chargesList, chargeId);
    }

    public Charge getCharge(int index) {
        return getChargesList().get(index);
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

    @Override
    public String getTypeUrl() {
        return "payments";
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    public List<Charge> getChargesList() {
        return chargesList;
    }

    public void setChargesList(List<Charge> chargesList) {
        this.chargesList = chargesList;
    }

    public List<Payout> getPayoutList() {
        return payoutList;
    }

    public void setPayoutList(List<Payout> payoutList) {
        this.payoutList = payoutList;
    }

    private <T extends PaymentType> T findById(Collection<T> collection, String id) {
        if (collection == null) {
            return null;
        }

        return collection.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .findAny()
                .orElse(null);
    }
}
