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

import java.lang.reflect.Type;
import java.util.Currency;

public class JsonCurrencyConverter
		implements JsonDeserializer<Currency>, JsonSerializer<Currency> {

	@Override
	public Currency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		return Currency.getInstance(json.getAsJsonPrimitive().getAsString());
	}

	@Override
	public JsonElement serialize(Currency src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getCurrencyCode());
	}

}
