package com.heidelpay.payment.communication;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.heidelpay.payment.webhook.WebhookEventEnum;

public class JsonWebhookEnumListConverter implements JsonDeserializer<List<WebhookEventEnum>>, JsonSerializer<List<WebhookEventEnum>> {

	@Override
	public JsonElement serialize(List<WebhookEventEnum> src, Type typeOfSrc, JsonSerializationContext context) {
		JsonArray jsonArray = new JsonArray(src.size());
		for(WebhookEventEnum webhookEvent : src) {
			jsonArray.add(webhookEvent.getEventName());
		}
		return jsonArray;
	}

	@Override
	public List<WebhookEventEnum> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		List<WebhookEventEnum> result = new ArrayList<WebhookEventEnum>();
		try {
			if(json.isJsonArray()) {
				JsonArray jsonArray = json.getAsJsonArray();
				for(JsonElement jsonValue : jsonArray) {
					result.add(WebhookEventEnum.fromEventName(jsonValue.getAsString()));
				}
			}
			return result;
		} catch (Exception e) {
			throw new JsonParseException("Cannot parse webhook event " + json.toString() + ".");
		}
	}

}
