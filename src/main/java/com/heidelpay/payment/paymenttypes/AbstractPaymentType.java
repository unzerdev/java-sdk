package com.heidelpay.payment.paymenttypes;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.communication.HttpCommunicationException;

public abstract class AbstractPaymentType implements PaymentType {
	private String id;
	
	private transient Heidelpay heidelpay;
	
	public AbstractPaymentType(Heidelpay heidelpay) {
		super();
		this.setHeidelpay(heidelpay);
	}

	public AbstractPaymentType() {
		super();
	}

	public abstract String getTypeUrl();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Heidelpay getHeidelpay() {
		return heidelpay;
	}

	public void setHeidelpay(Heidelpay heidelpay) {
		this.heidelpay = heidelpay;
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
	// Currently returnUrl is mandatory	
//		public Charge charge(BigDecimal amount, Currency currency) throws HttpCommunicationException {
//			return getHeidelpay().charge(amount, currency, this);
//		}
//		public Authorization authorize(BigDecimal amount, Currency currency) throws HttpCommunicationException {
//			return authorize(amount, currency, (URL)null, (Customer)null);
//		}
		


}
