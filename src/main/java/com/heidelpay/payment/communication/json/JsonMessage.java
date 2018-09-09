package com.heidelpay.payment.communication.json;

public class JsonMessage {
	private String code;
	private String customerMessage;
	private String merchantMessage;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCustomerMessage() {
		return customerMessage;
	}
	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}
	public String getMerchantMessage() {
		return merchantMessage;
	}
	public void setMerchantMessage(String merchantMessage) {
		this.merchantMessage = merchantMessage;
	}
	
}
