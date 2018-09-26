package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;

import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.PaymentType;

public class Payment extends AbstractPayment {
	
	public enum State {
		completed, pending, canceled, partly, payment_review, chargeback
	}
	
	private State paymentState;
	private BigDecimal amountTotal;
	private BigDecimal amountCharged;
	private BigDecimal amountCanceled;
	private BigDecimal amountRemaining;
	
	private Currency currency;
	private String orderId;
	
	private String customerId;
	private Customer customer;
	private String paymentTypeId;
	private PaymentType paymentType;
	
	private Authorization authorization;
	private List<Charge> chargesList;
	private List<Cancel> cancelList;

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
	
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, customerId);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, paymentType);
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
		return getAuthorization().cancel();
	}
	public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
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
	public Cancel getCancel(String chargeId, String refundId) {
		return new Cancel();
	}
	public PaymentType getPaymentType() throws HttpCommunicationException {
		if (paymentType == null) {
			paymentType = fetchPaymentType(getPaymentTypeId());
		}
		return paymentType;
	}
	private PaymentType fetchPaymentType(String paymentTypeId) throws HttpCommunicationException {
		return getHeidelpay().fetchPaymentType(paymentTypeId);
	}

	public Customer getCustomer() throws HttpCommunicationException {
		if (customer == null) {
			customer = fetchCustomer(getCustomerId());
		}
		return customer;
	}
	private Customer fetchCustomer(String customerId) throws HttpCommunicationException {
		return getHeidelpay().fetchCustomer(customerId);
	}

	@Override
	public String getTypeUrl() {
		return "payments";
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<Charge> getChargesList() {
		return chargesList;
	}

	public void setChargesList(List<Charge> chargesList) {
		this.chargesList = chargesList;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
	}

	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
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

}
