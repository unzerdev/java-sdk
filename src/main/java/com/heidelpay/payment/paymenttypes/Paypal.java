package com.heidelpay.payment.paymenttypes;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class Paypal extends AbstractPaymentType implements PaymentType {

	@Override
	public String getTypeUrl() {
		return "types/paypal";
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
