package com.heidelpay.payment.communication.json;

public class JsonPaypal extends JsonIdObject implements JsonObject {
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
