package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonBigDecimalConverter implements JsonDeserializer<BigDecimal>, JsonSerializer<BigDecimal> {

	@Override
	public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String jsonValue = json.getAsJsonPrimitive().getAsString();
		if (jsonValue == null || "".equalsIgnoreCase(jsonValue)) {
			return null;
		}
		BigDecimal number = new BigDecimal(jsonValue);
		number.setScale(4, BigDecimal.ROUND_HALF_UP);
		return number;
	}

	@Override
	public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
		src.setScale(4, BigDecimal.ROUND_HALF_UP);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		decimalFormatSymbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(decimalFormatSymbols);
		df.setGroupingUsed(false);
		return new JsonPrimitive(df.format(src));
	}

}
