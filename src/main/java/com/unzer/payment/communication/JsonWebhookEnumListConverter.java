package com.unzer.payment.communication;

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

import com.google.gson.*;
import com.unzer.payment.webhook.WebhookEventEnum;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
	public List<WebhookEventEnum> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
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
