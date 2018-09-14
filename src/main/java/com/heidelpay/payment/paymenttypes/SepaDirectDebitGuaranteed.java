package com.heidelpay.payment.paymenttypes;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class SepaDirectDebitGuaranteed extends AbstractPaymentType implements PaymentType {
	private String iban;
	private String bic;
	private String holder;
	
	public SepaDirectDebitGuaranteed(String iban) {
		super();
		this.iban = iban;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	@Override
	public String getTypeUrl() {
		return "types/sepa-direct-debit-guaranteed";
	}
	
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, (Customer)null);
	}
	public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, this, returnUrl, customer);
	}

}
