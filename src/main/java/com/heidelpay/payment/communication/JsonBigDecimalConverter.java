package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
