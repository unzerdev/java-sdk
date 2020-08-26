package com.heidelpay.payment.webhook;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2020 Heidelpay GmbH
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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.JsonAdapter;
import com.heidelpay.payment.communication.JsonWebhookEnumConverter;
import com.heidelpay.payment.communication.JsonWebhookEnumListConverter;

public class Webhook {
	
	private String id;
	
	private String url;

	@JsonAdapter(value = JsonWebhookEnumConverter.class)
	private WebhookEventEnum event;

	@JsonAdapter(value = JsonWebhookEnumListConverter.class)
	private List<WebhookEventEnum> eventList;

	public Webhook(String url, WebhookEventEnum event) {
		this.url = url;
		this.event = event;
	}

	public Webhook(String url, List<WebhookEventEnum> eventList) {
		this.url = url;
		this.eventList = new ArrayList<WebhookEventEnum>(eventList);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<WebhookEventEnum> getEventList() {
		return eventList;
	}

	public void setEventList(List<WebhookEventEnum> eventList) {
		this.eventList = eventList;
	}

	public WebhookEventEnum getEvent() {
		return event;
	}

	public void setEvent(WebhookEventEnum event) {
		this.event = event;
	}

	public String getId() {
		return id;
	}
}
