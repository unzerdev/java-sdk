package com.heidelpay.payment.communication.json;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import java.util.Currency;
import java.util.List;

public class JsonPayment extends JsonIdObject {

	private JsonState state;
	private JsonAmount amount;
	private Currency currency;
	private String orderId;
	private JsonResources resources;
	private List<JsonTransaction> transactions;
	public JsonState getState() {
		return state;
	}
	public void setState(JsonState state) {
		this.state = state;
	}
	public JsonAmount getAmount() {
		return amount;
	}
	public void setAmount(JsonAmount amount) {
		this.amount = amount;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public JsonResources getResources() {
		return resources;
	}
	public void setResources(JsonResources resources) {
		this.resources = resources;
	}
	public List<JsonTransaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<JsonTransaction> transactions) {
		this.transactions = transactions;
	}


}
