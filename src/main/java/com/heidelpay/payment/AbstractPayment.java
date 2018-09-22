package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
