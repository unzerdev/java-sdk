package com.heidelpay.payment.communication.json;

public class JsonCard extends JsonIdObject implements JsonObject {
	private String number;
	private String cvc;
	private String expiryDate;
	
	public String getNumber() {
		return number;
	}
	public JsonCard setNumber(String number) {
		this.number = number;
		return this;
	}
	public String getCvc() {
		return cvc;
	}
	public JsonCard setCvc(String cvc) {
		this.cvc = cvc;
		return this;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public JsonCard setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
		return this;
	}
}
