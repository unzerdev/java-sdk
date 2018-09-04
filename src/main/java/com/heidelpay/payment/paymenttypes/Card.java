package com.heidelpay.payment.paymenttypes;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.AbstractPayment;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class Card extends AbstractPayment implements PaymentType {
	private String pan;
	private String cvc;
	private String expiryDate;
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
	
	public Authorization authorize(BigDecimal amount, Currency currency) throws HttpCommunicationException {
		return authorize(amount, currency, (Customer)null, (URL)null);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return authorize(amount, currency, (Customer)null, returnUrl);
	}
	public Authorization authorize(BigDecimal amount, Currency currency, Customer customer, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, this, customer, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency) {
		return getHeidelpay().charge(amount, currency, this);
	}
	public Charge charge(BigDecimal amount, Currency currency, Customer customer, URL returnUrl) {
		return getHeidelpay().charge(amount, currency, this, customer, returnUrl);
	}
	
	@Override
	public String getTypeUrl() {
		return "types/cards";
	}
}
