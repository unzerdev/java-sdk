package com.heidelpay.payment.business.paymenttypes;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.business.AbstractPayment;
import com.heidelpay.payment.business.Authorization;
import com.heidelpay.payment.business.Charge;
import com.heidelpay.payment.business.Customer;

public class Card extends AbstractPayment implements PaymentType {
	private String number;
	private String cvc;
	private String expiryDate;
	public Card(String number, String expiryDate) {
		super();
		this.number = number;
		this.expiryDate = expiryDate;
	}
	public Card(String number, String expiryDate, String cvc) {
		super();
		this.number = number;
		this.expiryDate = expiryDate;
		this.cvc = cvc;
	}
	public String getNumber() {
		return number;
	}
	public Card setNumber(String number) {
		this.number = number;
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
	
	public Authorization authorize(BigDecimal amount, Currency currency) {
		return getHeidelpay().authorize(amount, currency, this);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, Customer customer, URL returnUrl) {
		return getHeidelpay().authorize(amount, currency, this, customer, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency) {
		return getHeidelpay().charge(amount, currency, this);
	}
	public Charge charge(BigDecimal amount, Currency currency, Customer customer, URL returnUrl) {
		return getHeidelpay().charge(amount, currency, this, customer, returnUrl);
	}
}
