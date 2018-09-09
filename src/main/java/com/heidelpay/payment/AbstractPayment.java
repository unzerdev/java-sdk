package com.heidelpay.payment;

import java.net.URL;

import com.heidelpay.payment.paymenttypes.PaymentType;

public abstract class AbstractPayment implements PaymentType {
	private String id;
	
	private transient Payment payment;
	private transient Heidelpay heidelpay;
	private transient URL resourceUrl;
	private transient String type;
	
	public AbstractPayment(Heidelpay heidelpay) {
		super();
		this.setHeidelpay(heidelpay);
	}

	public AbstractPayment() {
		super();
	}

	public abstract String getTypeUrl();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Heidelpay getHeidelpay() {
		return heidelpay;
	}

	public void setHeidelpay(Heidelpay heidelpay) {
		this.heidelpay = heidelpay;
	}

	public URL getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(URL resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
