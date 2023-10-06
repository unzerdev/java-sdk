package com.unzer.payment.communication;


import com.google.gson.JsonObject;
import com.unzer.payment.webhook.WebhookEventEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
