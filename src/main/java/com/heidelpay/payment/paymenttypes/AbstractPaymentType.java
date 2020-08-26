package com.heidelpay.payment.paymenttypes;

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

import com.heidelpay.payment.GeoLocation;
import com.heidelpay.payment.Heidelpay;

public abstract class AbstractPaymentType implements PaymentType {
	private String id;
	private Boolean recurring;
	private GeoLocation geoLocation;
	
	private transient Heidelpay heidelpay;
	
	public AbstractPaymentType(Heidelpay heidelpay) {
		super();
		this.setHeidelpay(heidelpay);
	}

	public AbstractPaymentType() {
		super();
	}

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

	public Boolean getRecurring() {
		return recurring;
	}

	public void setRecurring(Boolean recurring) {
		this.recurring = recurring;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
}
