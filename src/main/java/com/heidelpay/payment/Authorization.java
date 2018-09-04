package com.heidelpay.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

public class Authorization extends AbstractPayment {
	private BigDecimal amount;
	private Currency currency;
	private String customerId;
	private Resources resources = new Resources();
	private Processing processing = new Processing();
	private URL returnUrl;
	
	public Authorization() {
		super();
	}

	public Authorization(Heidelpay heidelpay) {
		super(heidelpay);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Authorization setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public Currency getCurrency() {
		return currency;
	}

	public Authorization setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	public URL getReturnUrl() {
		return returnUrl;
	}

	public Authorization setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}

	public Charge charge() {
		return getHeidelpay().chargeAuthorization(getPayment().getId());
	}

	public Charge charge(BigDecimal amount) {
		return getHeidelpay().chargeAuthorization(getPayment().getId(), amount);
	}

	public Cancel cancel() {
		return getHeidelpay().cancelAuthorization(getPayment().getId());
	}

	public Cancel cancel(BigDecimal amount) {
		return getHeidelpay().cancelAuthorization(getPayment().getId(), amount);
	}

	@Override
	public String getTypeUrl() {
		return "payments/authorize";
	}

	public Resources getResources() {
		return resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}
}
