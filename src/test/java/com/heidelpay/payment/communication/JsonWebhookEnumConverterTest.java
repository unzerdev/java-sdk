package com.heidelpay.payment.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.heidelpay.payment.webhook.WebhookEventEnum;

public class JsonWebhookEnumConverterTest {

	@Test
	public void testDeserializeWithInvalidJson() {
		JsonWebhookEnumConverter converter = new JsonWebhookEnumConverter();
		
		WebhookEventEnum webhookEventEnum = converter.deserialize(null, null, null);
		assertNull(webhookEventEnum);
		
		JsonObject json = new JsonObject();
		webhookEventEnum = converter.deserialize(json, null, null);
		assertNull(webhookEventEnum);
		
		json = new JsonObject();
		json.addProperty("webhook", WebhookEventEnum.ALL.name());
		
		webhookEventEnum = converter.deserialize(json.get("webhook"), null, null);
		assertEquals(WebhookEventEnum.ALL, webhookEventEnum);
	}
}
