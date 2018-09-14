package com.heidelpay.payment.paymenttypes;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;

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
	
	public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, returnUrl, (Customer)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, this, returnUrl, customer);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, (Customer)null);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer);
	}

}
