package com.heidelpay.payment.communication.json;

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
