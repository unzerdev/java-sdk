package com.unzer.payment.communication;

import com.google.gson.*;
import com.unzer.payment.webhook.WebhookEventEnum;

import java.lang.reflect.Type;

public class JsonWebhookEnumConverter implements JsonDeserializer<WebhookEventEnum>, JsonSerializer<WebhookEventEnum> {

    @Override
    public JsonElement serialize(WebhookEventEnum src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getEventName());
    }

    @Override
    public WebhookEventEnum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json == null || json.isJsonNull() || !json.isJsonPrimitive()) {
            return null;
        }

        String jsonValue = json.getAsString();
        return WebhookEventEnum.fromEventName(jsonValue);
    }

}
