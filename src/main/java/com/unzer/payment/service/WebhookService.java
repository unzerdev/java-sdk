package com.unzer.payment.service;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.Unzer;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.webhook.Webhook;
import com.unzer.payment.webhook.WebhookList;

public class WebhookService {
	private static final String WEBHOOK_BASE_URL = "webhooks";
	
	private UnzerRestCommunication restCommunication;
	private UrlUtil urlUtil;
	private Unzer unzer;
	private JsonParser jsonParser;
	
	public WebhookService(Unzer unzer, UnzerRestCommunication restCommunication) {
		this.unzer = unzer;
		this.urlUtil = new UrlUtil(unzer.getEndPoint());
		this.restCommunication = restCommunication;
		this.jsonParser = new JsonParser();
	}
	
	/**
	 * Register single webhook event
	 * 
	 * @param registerRequest
	 * @return
	 * @throws HttpCommunicationException
	 */
	public Webhook registerSingleWebhook(Webhook registerRequest) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), unzer.getPrivateKey(), registerRequest);
		return jsonParser.fromJson(response, Webhook.class);
	}
	
	/**
	 * Register multi webhook event
	 * 
	 * @param registerRequest
	 * @return
	 * @throws HttpCommunicationException
	 */
	public WebhookList registerMultiWebhooks(Webhook registerRequest) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), unzer.getPrivateKey(), registerRequest);
		return jsonParser.fromJson(response, WebhookList.class);
	}
	
	public WebhookList getWebhooks() throws HttpCommunicationException {
		String response = restCommunication.httpGet(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), unzer.getPrivateKey());
		return jsonParser.fromJson(response, WebhookList.class);
	}
	
	public Webhook deleteSingleWebhook(String webhookId) throws HttpCommunicationException {
		String deleteId = "";
		if(webhookId != null && !webhookId.trim().isEmpty()) {
			deleteId = webhookId;
		}
		String response = restCommunication.httpDelete(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL.concat("/").concat(deleteId)), unzer.getPrivateKey());
		return jsonParser.fromJson(response, Webhook.class);
	}
	
	/**
	 * Delete single or multi webhook(s).
	 * 
	 * @param webhookId null or emtpy if deleting all webhook(s).
	 * 
	 * @return
	 * @throws HttpCommunicationException 
	 */
	public WebhookList deleteMultiWebhook() throws HttpCommunicationException {
		String response = restCommunication.httpDelete(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), unzer.getPrivateKey());
		return jsonParser.fromJson(response, WebhookList.class);
	}
	
	public Webhook updateSingleWebhook(String updateId, Webhook updateWebhook) throws HttpCommunicationException {
		String response = restCommunication.httpPut(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL.concat("/").concat(updateId)), unzer.getPrivateKey(), updateWebhook);
		return jsonParser.fromJson(response, Webhook.class);
	}
}