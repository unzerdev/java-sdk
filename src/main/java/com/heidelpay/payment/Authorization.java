package com.heidelpay.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

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

	private Processing processing = new Processing();
	
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

}
