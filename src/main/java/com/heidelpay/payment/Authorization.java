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

public class Authorization extends AbstractPayment {
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

	public Authorization() {
		super();
	}

	public Authorization(Heidelpay heidelpay) {
		super(heidelpay);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Authorization setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public Currency getCurrency() {
		return currency;
	}

	public Authorization setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	public URL getReturnUrl() {
		return returnUrl;
	}

	public Authorization setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}
	public String getTypeId() {
		return typeId;
	}

	public Authorization setTypeId(String typeId) {
		this.typeId = typeId;
		return this;
	}

	public String getCustomerId() {
		return customerId;
	}

	public Authorization setCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public Authorization setMetadataId(String metadataId) {
		this.metadataId = metadataId;
		return this;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public Authorization setPaymentId(String paymentId) {
		this.paymentId = paymentId;
		return this;
	}

	public String getRiskId() {
		return riskId;
	}

	public Authorization setRiskId(String riskId) {
		this.riskId = riskId;
		return this;
	}

	public Processing getProcessing() {
		return processing;
	}

	public Authorization setProcessing(Processing processing) {
		this.processing = processing;
		return this;
	}


	public Charge charge() throws HttpCommunicationException {
		return getHeidelpay().chargeAuthorization(getPayment().getId());
	}

	public Charge charge(BigDecimal amount) throws HttpCommunicationException {
		return getHeidelpay().chargeAuthorization(getPayment().getId(), amount);
	}

	public Cancel cancel() throws HttpCommunicationException {
		return getHeidelpay().cancelAuthorization(getPayment().getId());
	}

	public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
		return getHeidelpay().cancelAuthorization(getPayment().getId(), amount);
	}

	@Override
	public String getTypeUrl() {
		return "payments/<paymentId>/authorize";
	}

	public URL getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(URL redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public List<Cancel> getCancelList() {
		return cancelList;
	}

	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
	}

}
