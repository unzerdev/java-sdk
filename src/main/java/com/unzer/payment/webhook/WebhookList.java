package com.unzer.payment.webhook;

import java.util.List;

/**
 * WebhookList contains list of Webhooks returned by Payment API
 */
public class WebhookList {

  private List<Webhook> events;

  public List<Webhook> getEvents() {
    return events;
  }
}
