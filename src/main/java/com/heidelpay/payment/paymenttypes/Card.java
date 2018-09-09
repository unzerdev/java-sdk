package com.heidelpay.payment.paymenttypes;

public class Card extends AbstractPaymentType implements PaymentType {
	private String pan;
	private String cvc;
	private String expiryDate;
	private String brand;
	
	public Card(String number, String expiryDate) {
		super();
		this.pan = number;
		this.expiryDate = expiryDate;
	}
	public Card(String number, String expiryDate, String cvc) {
		super();
		this.pan = number;
		this.expiryDate = expiryDate;
		this.cvc = cvc;
	}
	public String getNumber() {
		return pan;
	}
	public Card setNumber(String number) {
		this.pan = number;
		return this;
	}
	public String getCvc() {
		return cvc;
	}
	public Card setCvc(String cvc) {
		this.cvc = cvc;
		return this;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public Card setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
		return this;
	}
	@Override
	public String getTypeUrl() {
		return "types/cards";
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
}
