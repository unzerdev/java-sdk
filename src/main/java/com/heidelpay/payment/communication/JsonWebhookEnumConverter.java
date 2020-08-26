package com.heidelpay.payment.communication;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.heidelpay.payment.webhook.WebhookEventEnum;

public class JsonWebhookEnumConverter implements JsonDeserializer<WebhookEventEnum>, JsonSerializer<WebhookEventEnum> {

	@Override
	public JsonElement serialize(WebhookEventEnum src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getEventName());
	}

	@Override
	public WebhookEventEnum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String jsonValue = json.getAsJsonPrimitive().getAsString();
		if (jsonValue == null || "".equalsIgnoreCase(jsonValue)) {
			return null;
		}
		
		try {
			return WebhookEventEnum.fromEventName(jsonValue);
		} catch (Exception e) {
			throw new JsonParseException("Cannot parse webhook event " + jsonValue + ".");
		}
	}

}
