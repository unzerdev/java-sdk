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
}
