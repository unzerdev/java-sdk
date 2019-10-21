package com.heidelpay.payment.communication.json;

import com.google.gson.annotations.SerializedName;

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

public class JsonCard extends JsonIdObject implements JsonObject {
	private String number;
	private String cvc;
	private String expiryDate;
	@SerializedName("3ds")
	private Boolean threeDs;
	private String brand;
	private String method;
	private String cardHolder;
	private JsonCardDetails cardDetails;
	
	public String getNumber() {
		return number;
	}
	public JsonCard setNumber(String number) {
		this.number = number;
		return this;
	}
	public String getCvc() {
		return cvc;
	}
	public JsonCard setCvc(String cvc) {
		this.cvc = cvc;
		return this;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public JsonCard setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
		return this;
	}
	public Boolean get3ds() {
		return threeDs;
	}
	public void set3ds(Boolean threeDs) {
		this.threeDs = threeDs;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public JsonCardDetails getCardDetails() {
		return cardDetails;
	}

	public void setCardDetails(JsonCardDetails cardDetails) {
		this.cardDetails = cardDetails;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}
