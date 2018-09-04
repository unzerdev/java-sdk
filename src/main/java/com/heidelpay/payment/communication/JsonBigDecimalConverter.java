package com.heidelpay.payment.communication;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonBigDecimalConverter
		implements JsonDeserializer<BigDecimal>, JsonSerializer<BigDecimal>, InstanceCreator<BigDecimal> {

	@Override
	public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return new BigDecimal(json.getAsJsonPrimitive().getAsString());
	}

	@Override
	public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

	@Override
	public BigDecimal createInstance(Type type) {
		return new BigDecimal(1);
	}
}
