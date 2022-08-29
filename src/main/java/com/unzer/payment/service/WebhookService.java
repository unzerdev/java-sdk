package com.unzer.payment.service;

import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
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
        if (webhookId != null && !webhookId.trim().isEmpty()) {
            deleteId = webhookId;
        }
        String response = restCommunication.httpDelete(urlUtil.getRestUrl().concat(WEBHOOK_BASE_URL.concat("/").concat(deleteId)), unzer.getPrivateKey());
        return jsonParser.fromJson(response, Webhook.class);
    }

    /**
     * Delete single or multi webhook(s).
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
