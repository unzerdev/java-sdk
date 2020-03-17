package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
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
import java.util.Currency;
import java.util.List;

/**
 * Business abstract object for Authorization and Charge. Amount, currency and typeId are mandatory parameter to
 * execute an Authorization or Charge.
 * 
 * The returnUrl is mandatory in case of redirectPayments like Sofort, Paypal, Giropay, Creditcard 3DS
 * @author anna.sadriu
 *
 */
public abstract class AbstractInitPayment extends AbstractPayment {

	public enum Status {SUCCESS, PENDING, ERRROR}

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

	private List<Cancel> cancelList;

	private String traceId;

	public AbstractInitPayment() {
		super();
	}

	public AbstractInitPayment(Heidelpay heidelpay) {
		super(heidelpay);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public AbstractInitPayment setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public Currency getCurrency() {
		return currency;
	}

	public AbstractInitPayment setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	public URL getReturnUrl() {
		return returnUrl;
	}

	public AbstractInitPayment setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}

	public Boolean getCard3ds() {
		return card3ds;
	}

	public AbstractInitPayment setCard3ds(Boolean card3ds) {
		this.card3ds = card3ds;
		return this;
	}

	public String getOrderId() {
		return orderId;
	}

	public AbstractInitPayment setOrderId(String orderId) {
		this.orderId = orderId;
		return this;
	}

	public String getTypeId() {
		return typeId;
	}

	public AbstractInitPayment setTypeId(String typeId) {
		this.typeId = typeId;
		return this;
	}

	public String getCustomerId() {
		return customerId;
	}

	public AbstractInitPayment setCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public AbstractInitPayment setMetadataId(String metadataId) {
		this.metadataId = metadataId;
		return this;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public AbstractInitPayment setPaymentId(String paymentId) {
		this.paymentId = paymentId;
		return this;
	}

	public String getRiskId() {
		return riskId;
	}

	public AbstractInitPayment setRiskId(String riskId) {
		this.riskId = riskId;
		return this;
	}

	public String getBasketId() {
		return basketId;
	}

	public AbstractInitPayment setBasketId(String basketId) {
		this.basketId = basketId;
		return this;
	}

	public Status getStatus() {
		return status;
	}

	public AbstractInitPayment setStatus(Status status) {
		this.status = status;
		return this;
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

	public AbstractInitPayment setProcessing(Processing processing) {
		this.processing = processing;
		return this;
	}

	public List<Cancel> getCancelList() {
		return cancelList;
	}

	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
	}

	public Cancel getCancel(String cancelId) {
		if (cancelList == null) return null;
		for (Cancel cancel : cancelList) {
			if (cancelId.equalsIgnoreCase(cancel.getId())) {
				return cancel;
			}
		}
		return null;
	}

	public abstract String getTypeUrl();

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

}
