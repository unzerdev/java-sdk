package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.paymenttypes.PaymentType;

/**
 * Business object for a Payment. A Payment is the object that holds toghether several 
 * requests over time. This means that a payment belongs to one offer from the merchant.
 * 
 *  Within the Payment you also find the list of Charges, Cancels and the Authorization object.
 *  
 * @author rene.felder
 *
 */
public class Payment extends AbstractPayment {
	
	private Authorization authorization;
	private List<Charge> chargesList;
	private List<Cancel> cancelList;
	private List<Payout> payoutList;
	
	public Payment() {
		super();
	}

	public Payment(Heidelpay heidelpay) {
		super(heidelpay);
	}
	
	public Charge charge() throws HttpCommunicationException {
		return getHeidelpay().chargeAuthorization(getId());
	}

	public Charge charge(BigDecimal amount) throws HttpCommunicationException {
		return getHeidelpay().chargeAuthorization(getId(), amount);
	}
	
	public Charge charge(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId);
	}

	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId, customerId);
	}

	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, paymentType);
	}

	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId, returnUrl);
	}

	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId, returnUrl, customerId);
	}

	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, paymentType, returnUrl, customer);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, returnUrl);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, returnUrl, customerId);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, paymentType, returnUrl, customer);
	}

	public Cancel cancel() throws HttpCommunicationException {
		if (getAuthorization() == null) {
			List<PaymentError> paymentErrorList = new ArrayList<PaymentError>();
			paymentErrorList.add(new PaymentError(
							CANCEL_IS_ONLY_POSSIBLE_FOR_AN_AUTHORIZATION,
							PAYMENT_CANCELLATION_NOT_POSSIBLE,
							""));

			throw new PaymentException(paymentErrorList, "");
		}
		return getAuthorization().cancel();
	}

	public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
		if (getAuthorization() == null) {
			List<PaymentError> paymentErrorList = new ArrayList<PaymentError>();
			paymentErrorList.add(new PaymentError(
							CANCEL_IS_ONLY_POSSIBLE_FOR_AN_AUTHORIZATION,
							PAYMENT_CANCELLATION_NOT_POSSIBLE,
							""));

			throw new PaymentException(paymentErrorList, "");
		}
		return getAuthorization().cancel(amount);
	}
	
	public Authorization getAuthorization() {
		return authorization;
	}

	public Charge getCharge(String chargeId) {
		if (chargesList == null) return null;
		for (Charge charge : chargesList) {
			if (chargeId.equalsIgnoreCase(charge.getId())) {
				return charge;
			}
		}
		return null;
	}

	public Charge getCharge(int index) {
		return getChargesList().get(index);
	}

	public Payout getPayout(String payoutId) {
		if (payoutList == null) return null;
		for (Payout payout : payoutList) {
			if (payoutId.equalsIgnoreCase(payout.getId())) {
				return payout;
			}
		}
		return null;
	}

	public List<Cancel> getCancelList() {
		return cancelList;
	}

	public Cancel getCancel(String cancelId) {
		if (getCancelList() == null) return null;
		for (Cancel cancel : getCancelList()) {
			if (cancelId.equalsIgnoreCase(cancel.getId())) {
				return cancel;
			}
		}
		return null;
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

	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
	}

	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
	}

	public List<Payout> getPayoutList() {
		return payoutList;
	}

	public void setPayoutList(List<Payout> payoutList) {
		this.payoutList = payoutList;
	}
}
