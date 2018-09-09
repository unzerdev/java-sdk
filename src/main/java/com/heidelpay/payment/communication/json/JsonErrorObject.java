package com.heidelpay.payment.communication.json;

import java.util.List;

import com.heidelpay.payment.PaymentError;

public class JsonErrorObject {
	private String url;
	private String timestamp;
	private List<PaymentError> errors;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public List<PaymentError> getErrors() {
		return errors;
	}
	public void setErrors(List<PaymentError> errors) {
		this.errors = errors;
	}
	
}
