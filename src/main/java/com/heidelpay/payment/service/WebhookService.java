package com.heidelpay.payment.service;

import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.communication.HeidelpayRestCommunication;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.JsonParser;
import com.heidelpay.payment.webhook.Webhook;
import com.heidelpay.payment.webhook.WebhookList;

public class WebhookService {
	private static final String WEBHOOK_BASE_URL = "webhooks";
	
	private HeidelpayRestCommunication restCommunication;
	private UrlUtil urlUtil;
	private Heidelpay heidelpay;
	
	public WebhookService(Heidelpay heidelpay, HeidelpayRestCommunication restCommunication) {
		this.heidelpay = heidelpay;
		this.urlUtil = new UrlUtil(heidelpay.getEndPoint());
		this.restCommunication = restCommunication;
	}
	
	/**
	 * Register single webhook event
	 * 
	 * @param registerRequest
	 * @return
	 * @throws HttpCommunicationException
	 */
	public Webhook registerSingleWebhook(Webhook registerRequest) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), heidelpay.getPrivateKey(), registerRequest);
		return new JsonParser<Webhook>().fromJson(response, Webhook.class);
	}
	
	/**
	 * Register multi webhook event
	 * 
	 * @param registerRequest
	 * @return
	 * @throws HttpCommunicationException
	 */
	public WebhookList registerMultiWebhooks(Webhook registerRequest) throws HttpCommunicationException {
		String response = restCommunication.httpPost(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), heidelpay.getPrivateKey(), registerRequest);
		return new JsonParser<WebhookList>().fromJson(response, WebhookList.class);
	}
	
	public WebhookList getWebhooks() throws HttpCommunicationException {
		String response = restCommunication.httpGet(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), heidelpay.getPrivateKey());
		return new JsonParser<WebhookList>().fromJson(response, WebhookList.class);
	}
	
	public Webhook deleteSingleWebhook(String webhookId) throws HttpCommunicationException {
		String deleteId = "";
		if(webhookId != null && !webhookId.trim().isEmpty()) {
			deleteId = webhookId;
		}
		String response = restCommunication.httpDelete(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL.concat("/").concat(deleteId)), heidelpay.getPrivateKey());
		return new JsonParser<Webhook>().fromJson(response, Webhook.class);
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
		String response = restCommunication.httpDelete(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL), heidelpay.getPrivateKey());
		return new JsonParser<WebhookList>().fromJson(response, WebhookList.class);
	}
	
	public Webhook updateSingleWebhook(String updateId, Webhook updateWebhook) throws HttpCommunicationException {
		String response = restCommunication.httpPut(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL.concat("/").concat(updateId)), heidelpay.getPrivateKey(), updateWebhook);
		return new JsonParser<Webhook>().fromJson(response, Webhook.class);
	}
}
