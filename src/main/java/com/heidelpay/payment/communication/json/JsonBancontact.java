package com.heidelpay.payment.communication.json;

public class JsonBancontact extends JsonIdObject implements JsonObject {
	private String holder;
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
}
