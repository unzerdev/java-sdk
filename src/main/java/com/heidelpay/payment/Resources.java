package com.heidelpay.payment;

public class Resources {
	private Customer customer;
	private String typeId;
	private String customerId;
	private String metadataId;
	private String paymentId;
	private String riskId;
	
	public String getTypeId() {
		return typeId;
	}
	public Resources setTypeId(String typeId) {
		this.typeId = typeId;
		return this;
	}
	public String getCustomerId() {
		return customerId;
	}
	public Resources setCustomerId(String customerId) {
		this.customerId = customerId;
		return this;
	}
	public String getMetadataId() {
		return metadataId;
	}
	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getRiskId() {
		return riskId;
	}
	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

}
