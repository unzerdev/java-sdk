package com.heidelpay.payment.communication.json;

public class JsonCardDetails {

	private String cardType;

	private String account;

	private String countryIsoA2;

	private String countryName;

	private String issuerName;

	private String issuerUrl;

	private String issuerPhoneNumber;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCountryIsoA2() {
		return countryIsoA2;
	}

	public void setCountryIsoA2(String countryIsoA2) {
		this.countryIsoA2 = countryIsoA2;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getIssuerUrl() {
		return issuerUrl;
	}

	public void setIssuerUrl(String issuerUrl) {
		this.issuerUrl = issuerUrl;
	}

	public String getIssuerPhoneNumber() {
		return issuerPhoneNumber;
	}

	public void setIssuerPhoneNumber(String issuerPhoneNumber) {
		this.issuerPhoneNumber = issuerPhoneNumber;
	}
}
