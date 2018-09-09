package com.heidelpay.payment.communication.json;

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
