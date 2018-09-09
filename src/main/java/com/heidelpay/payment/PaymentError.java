package com.heidelpay.payment;

public class PaymentError {
	
	private String merchantMessage;
	private String customerMessage;
	private String code;
	
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "{merchantMessage:" + merchantMessage + ", customerMessage:" + customerMessage + ", code:" + code + "}";
	}
	
}
