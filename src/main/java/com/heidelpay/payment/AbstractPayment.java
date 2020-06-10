package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.net.URL;
import java.util.Date;

import com.heidelpay.payment.paymenttypes.PaymentType;

public abstract class AbstractPayment implements PaymentType {
	private String id;
	
	private transient Payment payment;
	private transient Heidelpay heidelpay;
	private transient URL resourceUrl;
	private transient String type;
	private Message message;

	private Date date;
	
	public AbstractPayment(Heidelpay heidelpay) {
		super();
		this.setHeidelpay(heidelpay);
	}

	public AbstractPayment() {
		super();
	}

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

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public AbstractPayment setDate(Date date) {
		this.date = date;
		return this;
	}
}
