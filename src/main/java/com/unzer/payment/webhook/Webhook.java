package com.unzer.payment.webhook;

import com.google.gson.annotations.JsonAdapter;
import com.unzer.payment.communication.JsonWebhookEnumConverter;
import com.unzer.payment.communication.JsonWebhookEnumListConverter;
import java.util.ArrayList;
import java.util.List;

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
