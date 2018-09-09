package com.heidelpay.payment.communication.json;

public class JsonError {
	private String code;
	private String merchantMessage;
	private String customerMessage;
	private String customer;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMerchantMessage() {
		return merchantMessage;
	}
	public void setMerchantMessage(String merchantMessage) {
		this.merchantMessage = merchantMessage;
	}
	public String getCustomerMessage() {
		return customerMessage;
	}
	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
}
