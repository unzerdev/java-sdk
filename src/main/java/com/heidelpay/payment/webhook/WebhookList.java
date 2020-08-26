package com.heidelpay.payment.webhook;

import java.util.List;

public class WebhookList {
	
	private List<Webhook> events;

	public List<Webhook> getEvents() {
		return events;
	}

	public void setEvents(List<Webhook> events) {
		this.events = events;
	}
}
