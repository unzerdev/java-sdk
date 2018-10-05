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
import java.util.Currency;
import java.util.List;

import com.heidelpay.payment.communication.HttpCommunicationException;

public class Charge extends AbstractPayment {
	private BigDecimal amount;
	private Currency currency;
	private URL returnUrl;

	private String typeId;
	private String customerId;
	private String metadataId;
	private String paymentId;
	private String riskId;
	
	private URL redirectUrl;

	private Processing processing = new Processing();
	
	private List<Cancel> cancelList;
	
	public Charge() {
		super();
	}
	public Charge(Heidelpay heidelpay) {
		super(heidelpay);
	}
	
	public Cancel cancel() throws HttpCommunicationException {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId());
	}
	public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId(), amount);
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
	public BigDecimal getAmount() {
		return amount;
	}
	public Charge setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}
	public Currency getCurrency() {
		return currency;
	}
	public Charge setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}
	public String getTypeId() {
		return typeId;
	}
	public Charge setTypeId(String typeId) {
		this.typeId = typeId;
		return this;
	}
	public String getCustomerId() {
		return customerId;
	}
	public Charge setCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}
	public String getMetadataId() {
		return metadataId;
	}
	public Charge setMetadataId(String metadataId) {
		this.metadataId = metadataId;
		return this;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public Charge setPaymentId(String paymentId) {
		this.paymentId = paymentId;
		return this;
	}
	public String getRiskId() {
		return riskId;
	}
	public Charge setRiskId(String riskId) {
		this.riskId = riskId;
		return this;
	}
	public Processing getProcessing() {
		return processing;
	}
	public void setProcessing(Processing processing) {
		this.processing = processing;
	}
	public URL getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
	}

	@Override
	public String getTypeUrl() {
		return "payments/<paymentId>/charges";
	}
	public URL getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(URL redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
