package com.heidelpay.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;

public class Charge extends AbstractPayment {
	private BigDecimal amount;
	private Currency currency;

	private String typeId;
	private String customerId;
	private String metadataId;
	private String paymentId;
	private String riskId;

	private Processing processing = new Processing();
	private URL returnUrl;
	
	private List<Cancel> cancelList;
	
	public Charge() {
		super();
	}
	public Charge(Heidelpay heidelpay) {
		super(heidelpay);
	}
	
	public Cancel cancel() {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId());
	}
	public Cancel cancel(BigDecimal amount) {
		return getHeidelpay().cancelCharge(getPayment().getId(), getId(), amount);
	}
	public List<Cancel> getCancelList() {
		return cancelList;
	}
	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
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
}
