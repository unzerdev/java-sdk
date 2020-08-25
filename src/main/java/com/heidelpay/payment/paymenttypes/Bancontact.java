package com.heidelpay.payment.paymenttypes;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.json.JsonBancontact;
import com.heidelpay.payment.communication.json.JsonObject;

public class Bancontact extends AbstractPaymentType implements PaymentType {

	private String holder;
	
	public Bancontact(String holder) {
		this.holder = holder;
	}

	public Bancontact() {
		super();
	}

	public Bancontact(Heidelpay heidelpay) {
		super(heidelpay);
	}

	@Override
	public String getTypeUrl() {
		return "types/bancontact";
	}

	@Override
	public PaymentType map(PaymentType bancontact, JsonObject jsonId) {
		if(bancontact instanceof Bancontact && jsonId instanceof JsonBancontact) {
			((Bancontact) bancontact).setId(jsonId.getId());
			((Bancontact) bancontact).setRecurring(((JsonBancontact) jsonId).getRecurring());
			((Bancontact) bancontact).setHolder(((JsonBancontact) jsonId).getHolder());
		}
		return bancontact;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer);
	}

}
