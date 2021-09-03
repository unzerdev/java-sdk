package com.unzer.payment.communication;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - 2021 Unzer E-Com GmbH
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.unzer.payment.webhook.WebhookEventEnum;

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
