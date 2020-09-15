package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2020 Heidelpay GmbH
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

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
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
	public WebhookEventEnum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {		
		if(json == null || json.isJsonNull() || !json.isJsonPrimitive()) {
			return null;
		}
		
		String jsonValue = json.getAsString();
		return WebhookEventEnum.fromEventName(jsonValue);
	}

}
